/**
 * 工程名:JTools
 * 文件名:SQLDirectUtils.java
 * 包   名:qin.tool.sql
 * 日   期:2016年8月17日上午11:40:09
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.sql;

import android.support.annotation.NonNull;

import java.util.Iterator;

import qin.tool.sql.Column.TYPE;
import qin.tool.utils.StringUtils;

/**
 *类   名:SQLDirectUtils
 *功   能:TODO
 *
 *日  期:2016年8月17日 上午11:40:09
 * @author JeoyQin
 *
 */
public class SQLDirectUtils {
	private SQLDirectUtils() {
		// TODO Auto-generated constructor stub
	}
	
	public static SQLDirect checkTable(Table table) {
		SQLDirect d = null;
		if(!StringUtils.isEmpty(table.name)) {
			d = new SQLDirect("SELECT COUNT(*) AS c FROM sqlite_master WHERE type='table' AND name='" + table.name + "';");
		}
		return d;
	}
	
	/**
	 * 建表命令：
	 * CREATE TABLE 表名称
	＊(
	＊列名称1  数据类型,
	＊列名称2  数据类型,
	＊列名称3  数据类型,
	＊....
	＊)
	 */
	public static SQLDirect createTableDirect(Table table) throws IllegalArgumentException{
		SQLDirect d = new SQLDirect();
		if(table.mColumns.size() == 0) {
			throw new IllegalArgumentException("createTableDirect " + table.name + " data column size is zero!");
		}
			
		String sql = "CREATE TABLE IF NOT EXISTS " + table.name + "(";
		StringBuffer buffer = new StringBuffer(sql);
		Iterator<Column> columns = table.mColumns.values().iterator();
		boolean hasadd = false;
		while(columns.hasNext()) {
			Column c = columns.next();
			buffer.append((!hasadd ? "" : ", ")  + c.name);
			buffer.append(" " + c.getValueType().getValue());
			if(c.notNull) {
				buffer.append(" NOT NULL");
			}
			final String ctype = c.type.getValue();
			if(!StringUtils.isEmpty(ctype)) 
				buffer.append(" " + ctype);
			if(!hasadd) hasadd = true;
		}
		buffer.append(")");
		buffer.append(";");
		d.sql = buffer.toString();
		return d;
	}
	
	private static SQLDirect insertDirect(Table table, @NonNull Object obj) throws IllegalArgumentException{
		SQLDirect d = new SQLDirect();
		if(table.mColumns.size() == 0) {
			throw new IllegalArgumentException("insertDirect " + table.name + " data column size is zero!");
		}
		
		StringBuffer buffer = new StringBuffer("INSERT INTO  " + table.name + " (");
		StringBuffer values = new StringBuffer(" VALUES (");
		Iterator<Column> columns = table.mColumns.values().iterator();
		boolean hasadd = false;
		while(columns.hasNext()) {
			Column c = columns.next();
			final Object value = c.getValueFromObj(obj);
			d.push(value);
			
			buffer.append((!hasadd ? "" : ", ")  + c.name);
			values.append((!hasadd ? "" : ", ")  + "?");
			if(!hasadd) hasadd = true;
		}
		buffer.append(")");
		values.append(")");
		d.sql = buffer.toString() + values.toString() + ";";
		return d;
	}
	
	public static SQLDirect insertDirect(Table table, @NonNull Object obj, boolean distinct) throws IllegalArgumentException {
		if(!distinct) {
			return insertDirect(table, obj);
		}
		
		SQLDirect d = new SQLDirect();
		if(table.mColumns.size() == 0) {
			throw new IllegalArgumentException("insertDirect " + table.name + " data column size is zero!");
		}
		
		StringBuffer buffer = new StringBuffer("INSERT INTO  " + table.name + " (");
		StringBuffer values = new StringBuffer("");
		Iterator<Column> columns = table.mColumns.values().iterator();
		boolean hasadd = false;
		while(columns.hasNext()) {
			Column c = columns.next();
			final Object value = c.getValueFromObj(obj);
			d.push(value);
			
			buffer.append((!hasadd ? "" : ", ")  + c.name);
			values.append((!hasadd ? "" : ", ")  + "?");
			if(!hasadd) hasadd = true;
		}
		
		if(hasadd) {
			buffer.append(")");
			buffer.append(" SELECT ");
			buffer.append(values.toString());
		}

		buffer.append(" WHERE  NOT EXISTS (SELECT 1 FROM " + table.name + " WHERE ");
		hasadd = false;
		columns = table.mColumns.values().iterator();
		while(columns.hasNext()) {
			Column c = columns.next();
			if(c.type == TYPE.EQUALS 
					|| c.type == TYPE.UNIQUE
					|| c.type == TYPE.PRIMARY) {
				buffer.append(!hasadd ? "" : " AND ");
				buffer.append(c.name + " = ");
				buffer.append("?");
				d.push(c.getValueFromObj(obj));
				if(!hasadd) hasadd = true;
			}
		}
		buffer.append(");");
		d.sql = buffer.toString();
		return d;
	}
	
	/**
	 * replace into 操作数据库必须存在PRIMARY或者UNIQUE限制，否值replace无意义
	 */
	public static SQLDirect replaceDirect(Table table, @NonNull Object obj) throws IllegalArgumentException{
		SQLDirect d = new SQLDirect();
		if(table.mColumns.size() == 0) {
			throw new IllegalArgumentException("replaceDirect " + table.name + " data column size is zero!");
		}
		
		if(!table.hasPrimary && !table.hasUnique) {
			throw new SQLOperateException(table.name + " has not primary key or unique key");
		}
		
		StringBuffer buffer = new StringBuffer("REPLACE INTO  " + table.name + " (");
		StringBuffer values = new StringBuffer(" VALUES (");
		Iterator<Column> columns = table.mColumns.values().iterator();
		boolean hasadd = false;
		while(columns.hasNext()) {
			Column c = columns.next();
			final Object value = c.getValueFromObj(obj);
			d.push(value);
			
			buffer.append((!hasadd ? "" : ", ")  + c.name);
			values.append((!hasadd ? "" : ", ")  + "?");
			if(!hasadd) hasadd = true;
		}
		buffer.append(")");
		values.append(")");
		d.sql = buffer.toString() + values.toString();
		return d;
	}
	
	/**
	 * @param  select 筛选条件
	 * @param  newValue 更新条目
	 */
	public static <T> SQLDirect updateDirect(Table table, @NonNull T select, @NonNull T newValue) {
		StringBuffer buffer = new StringBuffer();
		Iterator<Column> columns = table.mColumns.values().iterator();
		boolean hasadd = false;
		while(columns.hasNext()) {
			Column c = columns.next();
			if(c.type == TYPE.EQUALS 
					|| c.type == TYPE.UNIQUE
					|| c.type == TYPE.PRIMARY) {
				buffer.append(!hasadd ? "" : " AND ");
				buffer.append(c.name + " = ");
				buffer.append(c.valueEntry.toValueName(c.getValueFromObj(select), c.defValue + ""));
				if(!hasadd) hasadd = true;
			}
		}
		String selection = buffer.toString();
		return updateDirect(table, newValue, selection, null);
	}
	
	/**
	 * @param newValue 需要更新的数据，更新筛选依据equals、unique、primary标记属性
	 */
	public static SQLDirect updateDirect(Table table, Object newValue) {
		return updateDirect(table, newValue, null);
	}
	
	/**
	 * SQL数据更新操作
	 * @param obj 将更新的新数据
	 * @param columnNames 数据列的名称，这些数据列将作为查询where判断的条件
	 */
	public static <T> SQLDirect updateDirect(Table table, @NonNull T obj, String[] columnNames) throws IllegalArgumentException{
		StringBuffer buffer = new StringBuffer();
		Iterator<Column> columns = table.mColumns.values().iterator();
		boolean equals = columnNames == null || columnNames.length <= 0;
		boolean hasadd = false;
		while(columns.hasNext()) {
			Column c = columns.next();
			if(equals) {
				if(c.type == TYPE.EQUALS 
						|| c.type == TYPE.UNIQUE
						|| c.type == TYPE.PRIMARY) {
					buffer.append(!hasadd ? "" : " AND ");
					buffer.append(c.name + " = ");
					buffer.append(c.valueEntry.toValueName(c.getValueFromObj(obj), c.defValue + ""));
					if(!hasadd) hasadd = true;
				}
			} else {
				if(StringUtils.findStringFromArray(columnNames, c.name)) {
					buffer.append(!hasadd ? "" : " AND ");
					buffer.append(c.name + " = ");
					buffer.append(c.valueEntry.toValueName(c.getValueFromObj(obj), c.defValue + ""));
					if(!hasadd) hasadd = true;
				}
			}
		}
		String selection = buffer.toString();
		return updateDirect(table, obj, selection, null);
	}
	
	public static <T> SQLDirect updateDirect (Table table, @NonNull T value, String selection, String[] selectArgs) throws IllegalArgumentException{
		if(table.mColumns.size() == 0) {
			throw new IllegalArgumentException("updateDirect(Object obj, String...columnNames) " + table.name + " data column size is zero!");
		}
		SQLDirect d = new SQLDirect();
		StringBuffer buffer = new StringBuffer("UPDATE  " + table.name + " SET ");
		Iterator<Column> columns = table.mColumns.values().iterator();
		boolean hasadd = false;
		while(columns.hasNext()) {
			Column c = columns.next();
			if(hasadd) {
				buffer.append(", ");
			}
			buffer.append(c.name + " = ?");
			d.push(c.getValueFromObj(value));
			if(!hasadd) hasadd = true;
		}
		
		if(!StringUtils.isEmpty(selection)) {
			buffer.append(" WHERE ");
			final int count = selectArgs != null ? selectArgs.length : 0;
			buffer.append(selection);
			for(int i = 0; i < count; i++) {
				d.push(selectArgs[i]);
			}
			buffer.append(" ");
		}
		if(buffer.length() > 0) buffer.append(";");
		d.sql = buffer.toString();
		return d;
	}
	
	public static SQLDirect deleteDirect(Table table) {
		SQLDirect d = null;
		if(!StringUtils.isEmpty(table.name)) {
			d = new SQLDirect("DELETE FROM " + table.name);
		}
		return d;
	}
	
	public static SQLDirect deleteDirect(Table table, @NonNull Object obj) {
		return deleteDirect(table, obj, null);
	}
	
	public static SQLDirect deleteDirect(Table table, @NonNull Object obj, String[] columnNames) {
		StringBuffer buffer = new StringBuffer();
		Iterator<Column> columns = table.mColumns.values().iterator();
		boolean equals = columnNames == null || columnNames.length <= 0;
		boolean hasadd = false;
		while(columns.hasNext()) {
			Column c = columns.next();
			if(equals) {
				if(c.type == TYPE.EQUALS 
						|| c.type == TYPE.UNIQUE
						|| c.type == TYPE.PRIMARY) {
					buffer.append(!hasadd ? "" : " AND ");
					buffer.append(c.name + " = ");
					buffer.append(c.valueEntry.toValueName(c.getValueFromObj(obj), c.defValue + ""));
					if(!hasadd) hasadd = true;
				}
			} else {
				if(StringUtils.findStringFromArray(columnNames, c.name)) {
					buffer.append(!hasadd ? "" : " AND ");
					buffer.append(c.name + " = ");
					buffer.append(c.valueEntry.toValueName(c.getValueFromObj(obj), c.defValue + ""));
					if(!hasadd) hasadd = true;
				}
			}
		}
		String selection = buffer.toString();
		return deleteDirect(table, obj, selection, null);
	}
	
	public static SQLDirect deleteDirect(Table table, @NonNull Object obj, String selection, String[] selectArgs) {
		if(table.mColumns.size() == 0) {
			throw new IllegalArgumentException("updateDirect(Object obj, String...columnNames) " + table.name + " data column size is zero!");
		}
		SQLDirect d = new SQLDirect();
		StringBuffer buffer = new StringBuffer("DELETE FROM " + table.name);
		
		if(!StringUtils.isEmpty(selection)) {
			buffer.append(" WHERE ");
			final int count = selectArgs != null ? selectArgs.length : 0;
			buffer.append(selection);
			for(int i = 0; i < count; i++) {
				d.push(selectArgs[i]);
			}
			buffer.append(" ");
		}
		if(buffer.length() > 0) buffer.append(";");
		d.sql = buffer.toString();
		return d;
	}
	
	public static SQLDirect queryDirect(Table table, @NonNull Class<?> clazz, boolean distinct) {
		return queryDirect(table, null, distinct, null, null, null, null, null, null, null, null);
	}
	
	public static SQLDirect queryDirect(Table table, @NonNull Object select, boolean distinct) {
		return queryDirect(table, select, null, distinct, null, null, null, null, null, null);
	}
	
	/**
	 * @param select 筛选值
	 * @param columnNames 筛选数据列名称，如为null时筛选列为EQUALS标记属性
	 * @param distinct 查询结果不重复限制
	 * @param groupByColumns 语句用于结合合计函数，根据一个或多个列对结果集进行分组
	 * @param having 统计条件限制
	 * @param orderBy 语句用于对结果集进行排序
	 * @param orderByColumns 排序限制条件
	 * @param limit LIMIT 子句用于限制由 SELECT 语句返回的数据量
	 * @param offset limit的条目限制
	 */
	public static SQLDirect queryDirect(Table table, @NonNull Object select, String[] columnNames, boolean distinct, String [] groupByColumns, 
			String having, String [] orderByColumns, String orderBy, String limit, String offset){
		Class<?> clazz = select.getClass();
		StringBuffer buffer = new StringBuffer();
		Iterator<Column> columns = table.mColumns.values().iterator();
		boolean equals = columnNames == null || columnNames.length <= 0;
		boolean hasadd = false;
		while(columns.hasNext()) {
			Column c = columns.next();
			if(equals) {
				if(c.type == TYPE.EQUALS 
						|| c.type == TYPE.UNIQUE
						|| c.type == TYPE.PRIMARY) {
					buffer.append(!hasadd ? "" : " AND ");
					buffer.append(c.name + " = ");
					buffer.append(c.valueEntry.toValueName(c.getValueFromObj(select), c.defValue + ""));
					if(!hasadd) hasadd = true;
				}
			} else {
				if(StringUtils.findStringFromArray(columnNames, c.name)) {
					buffer.append(!hasadd ? "" : " AND ");
					buffer.append(c.name + " = ");
					buffer.append(c.valueEntry.toValueName(c.getValueFromObj(select), c.defValue + ""));
					if(!hasadd) hasadd = true;
				}
			}
		}
		String selection = buffer.toString();
 		return queryDirect(table, null, distinct, selection, null, groupByColumns, having, orderByColumns, orderBy, limit, offset);
	}
	
	/**
	 * @param columnNames 查询列数据
	 * @param distinct 查询结果不重复限制
	 * @param selection where语句，占位使用＃
	 * @param selectArgs 替换占位符的值
	 * @param groupByColumns 语句用于结合合计函数，根据一个或多个列对结果集进行分组
	 * @param having 统计条件限制
	 * @param orderByColumns 排序限制条件
	 * @param orderBy 语句用于对结果集进行排序
	 * @param limit LIMIT 子句用于限制由 SELECT 语句返回的数据量
	 * @param offset offset limit的条目限制
	 */
	public static SQLDirect queryDirect(Table table, String[] columnNames, boolean distinct, String selection, String[] selectArgs, 
			String [] groupByColumns, String having, String [] orderByColumns, String orderBy, String limit, String offset) {
		SQLDirect d = new SQLDirect();
		StringBuffer buffer = new StringBuffer();
		buffer.append("SELECT ");
		if(distinct) {
			buffer.append(" DISTINCT ");
		}
		
		if(columnNames != null && columnNames.length > 0) {
			appendClause(buffer, columnNames);
		} else {
			buffer.append(" * ");
		}
		appendClause(buffer, "FROM ", table.name);
		//
		if(!StringUtils.isEmpty(selection)) {
			buffer.append(" WHERE ");
			buffer.append(selection);
			buffer.append(" ");
		} 
		
		final int count = selectArgs != null ? selectArgs.length : 0;
		for(int i = 0; i < count; i++) {
			d.pushName(selectArgs[i]);
		}
		
		if(groupByColumns != null && groupByColumns.length > 0) {
			buffer.append(" GROUP BY ");
			appendClause(buffer, groupByColumns);
		}
		
		if(!StringUtils.isEmpty(having)) {
			appendClause(buffer, " HAVING ", having);
			buffer.append(" ");
		}
		
		if(orderByColumns != null && orderByColumns.length > 0) {
			buffer.append(" ORDER BY ");
			appendClause(buffer, orderByColumns);
		}

		if(!StringUtils.isEmpty(orderBy)) {
			buffer.append(orderBy);
			buffer.append(" ");
		}
		
		if(!StringUtils.isEmpty(limit)) {
			appendClause(buffer, " LIMIT ", limit);
		}
		
		if(!StringUtils.isEmpty(offset)) {
			appendClause(buffer, " OFFSET ", offset);
		}
		d.sql = buffer.toString() + ";";
		return d;
	}
	
	public static void appendClause(@NonNull StringBuffer buffer, String name, String clause) {
		if(buffer == null || StringUtils.isEmpty(name) || StringUtils.isEmpty(clause)) return;
		buffer.append(name);
		buffer.append(clause);
	}
	
	public static void appendClause(@NonNull StringBuffer buffer, String...columns) {
		if(buffer == null || columns == null || columns.length == 0) return;
		int i = 0;
		for(String column : columns) {
			if(!StringUtils.isEmpty(column)) {
				if(i++ > 0) {
					buffer.append(", ");
				}
				buffer.append(column);
			}
		}
		buffer.append(" ");
	}
}
