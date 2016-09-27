/**
 * 工程名:JTools
 * 文件名:DbUtils.java
 * 包   名:qin.tool.sql
 * 日   期:2016年8月17日上午11:09:58
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.sql;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import qin.tool.utils.StringUtils;

/**
 *类   名:DbUtils
 *功   能:TODO
 *
 *日  期:2016年8月17日 上午11:09:58
 * @author JeoyQin
 *
 */
public class DbUtils {
	public static HashMap<DbDao, DbUtils> mDbUtils = new HashMap<DbDao, DbUtils>();
	public DbHelper mDbHelper;
	public HashMap<Class<?>, Table> mTables = new HashMap<Class<?>, Table>();
	
	public static DbUtils getDbUtils(@NonNull Context context) {
		if(context == null) {
			throw new IllegalArgumentException("getDbUtils \"Context\" can't null");
		}
		DbDao dao = new DbDao(context.getApplicationContext());
		return getDbUtils(dao);
	}
	
	/**
	 * @return the utils
	 */
	public static DbUtils getDbUtils(@NonNull DbDao dao) {
		if(dao == null) {
			throw new IllegalArgumentException("getDbUtils \"dao\" can't null");
		}
		DbUtils utils = null;
		if(mDbUtils.containsKey(dao)) {
			utils = mDbUtils.get(dao);
		}
		
		if(utils == null) {
			final DbHelper helper = new DbHelper(dao);
			utils = new DbUtils(helper);
		}
		return utils;
	}
	
	public DbUtils(DbHelper dbHelper) {
		mDbHelper = dbHelper;
		mDbUtils.put(dbHelper.dao, this);
	}
	

	Table  hasTable(@NonNull Class<?> clazz) {
		Table table = null;
		if(mTables.containsKey(clazz))
			table = mTables.get(clazz);
		
		if(table == null) {
			table = new Table(clazz);
			if(StringUtils.isEmpty(table.name)) {
				throw new SQLOperateException("DbUtils not support " + clazz.getName());
			}
			mTables.put(clazz, table);
		}
		return table;
	}
	
	/**
	 * 检查数据表是否存在，不存在则创建
	 */
	public void creatTable(@NonNull Class<?> clazz) {
		boolean created = tableExists(clazz);
		if(!created) {
			final Table table = hasTable(clazz);
			SQLDirect d = SQLDirectUtils.createTableDirect(table);
			writeDatabase(d);
			if(table != null && !table.exsit) table.exsit = true;
		}
	}
	
	/**
	 * 数据表是否处在
	 */
	boolean tableExists(@NonNull Class<?> clazz) {
		Table table = hasTable(clazz);
		if(table != null && !table.exsit) {
			SQLDirect d = SQLDirectUtils.checkTable(table);
			Cursor c = readDatabase(d);
			if(c != null && c.moveToNext()) {
				if(c.getInt(0) > 0) {
					table.exsit = true;
				}
			}
			if(c != null) c.close();
			d.close();
		}
		return table.exsit;
	}
	
	public <T> void  insertArray(boolean distinct, @NonNull T...objects) {
		synchronized (this) {
			if(objects == null || objects.length <= 0) return;
			Class<?> clazz = objects[0].getClass();
			creatTable(clazz);
			Table table = hasTable(clazz);
			SQLDirect d = new SQLDirect();
			if(table.exsit) {
				d.setSQLiteDatabase(mDbHelper.getWritableDatabase());
				d.beginTransaction();
				for(T t : objects) {
					SQLDirect sd = null;
					if(table.hasPrimary) {
						sd = SQLDirectUtils.replaceDirect(table, t);
					} else {
						sd = SQLDirectUtils.insertDirect(table, t, distinct);
					}
					d.sql = sd.sql;
					d.args = sd.args;
					d.argsNames = sd.argsNames;
					d.execSQL();
				}
				d.setTransactionSuccessful();
				d.endTransaction();
			}
		}
	}
	
	public <T> void  insertList(boolean distinct, @NonNull List<T> objects) {
		synchronized (this) {
			if(objects == null || objects.size() <= 0) return;
			Class<?> clazz = objects.get(0).getClass();
			creatTable(clazz);
			Table table = hasTable(clazz);
			SQLDirect d = new SQLDirect();
			if(table.exsit) {
				d.setSQLiteDatabase(mDbHelper.getWritableDatabase());
				d.beginTransaction();
				for(T t : objects) {
					SQLDirect sd = null;
					if(table.hasPrimary) {
						sd = SQLDirectUtils.replaceDirect(table, t);
					} else {
						sd = SQLDirectUtils.insertDirect(table, t, distinct);
					}
					d.sql = sd.sql;
					d.args = sd.args;
					d.argsNames = sd.argsNames;
					d.execSQL();
				}
				d.setTransactionSuccessful();
				d.endTransaction();
				d.clear();
			}
		}
	}
	
	/**
	 * 当对象属性没有Primary和Unique标注是使用，可重复插入
	 */
	public void insert(@NonNull Object object) {
		synchronized (this) {
			Class<?> clazz = object.getClass();
			creatTable(clazz);
			Table table = hasTable(clazz);
			if(table.exsit && (!table.hasPrimary && !table.hasUnique)) {
				SQLDirect d = SQLDirectUtils.insertDirect(table, object, false);
				writeDatabase(d);
			}
		}
	}
	
	/**
	 * 对象属性必须有Primary、Unique、Equals中一个或多个标注，不支持重复插入
	 */
	public void insertOrReplace(@NonNull Object object) {
		synchronized (this) {
			Class<?> clazz = object.getClass();
			creatTable(clazz);
			Table table = hasTable(clazz);
			SQLDirect d = null;
			if(table.hasPrimary) {
				d = SQLDirectUtils.replaceDirect(table, object);
				writeDatabase(d);
			} else {
				d = SQLDirectUtils.updateDirect(table,object);
				writeDatabase(d);
				d = SQLDirectUtils.insertDirect(table, object, true);
				writeDatabase(d);
			}
		}
	}

	/**
	 * 数据更新
	 * @param select 筛选条件
	 * @param newValue 新值
	 * @param <T>
     */
	public <T> void update(@NonNull T select, @NonNull T newValue) {
		synchronized (this) {
			Class<?> clazz = select.getClass();
			creatTable(clazz);
			Table table = hasTable(clazz);
			SQLDirect d = SQLDirectUtils.updateDirect(table, select, newValue);
			writeDatabase(d);
		}
	}

	/**
	 * 更新数据
	 * @param object 筛选条件及更新数据指定
	 * @param columns 筛选数据列限定
     */
	public void update(@NonNull Object object, String...columns) {
		synchronized (this) {
			Class<?> clazz = object.getClass();
			creatTable(clazz);
			Table table = hasTable(clazz);
			SQLDirect d = SQLDirectUtils.updateDirect(table, object, columns);
			writeDatabase(d);
		}
	}

	/**
	 *
	 * @param object
	 * @param selection
	 * @param selectArgs
     */
	public void update(@NonNull Object object, @NonNull String selection, String[] selectArgs) {
		synchronized (this) {
			Class<?> clazz = object.getClass();
			creatTable(clazz);
			Table table = hasTable(clazz);
			SQLDirect d = SQLDirectUtils.updateDirect(table, object, selection, selectArgs);
			writeDatabase(d);
		}
	}
	
	/**
	 * 删除所有数据
	 * @param clazz 数据类型限定
	 */
	public void delete(@NonNull Class<?> clazz) {
		synchronized (this) {
			creatTable(clazz);
			Table table = hasTable(clazz);
			SQLDirect d = SQLDirectUtils.deleteDirect(table);
			writeDatabase(d);
		}
	}
	
	/**
	 * 删除指定数据
	 * @param object 数据类型及筛选值限定
	 * @param columns 筛选数据列限定，不传值使用所有UNIQUE、PRIMARY、EQUALS标记列筛选
	 */
	public void delete(@NonNull Object object, String...columns) {
		synchronized (this) {
			if(columns == null) {
				delete(object);
				return;
			}
			Class<?> clazz = object.getClass();
			creatTable(clazz);
			Table table = hasTable(clazz);
			SQLDirect d = SQLDirectUtils.deleteDirect(table, object, columns);
			writeDatabase(d);
		}
	}
	
	/**
	 * @param clazz 查询数据类型
	 * @param distinct 数据结果不重复显示
	 * @param columnNames 关心数据列名称，不传值时查询所有数据列
	 */
	public <T> List<T> query(Class<T> clazz, boolean distinct, String...columnNames) {
		return query(clazz, columnNames, distinct, null, null, null, null, null, null, null, null);
	}
	
	/**
	 * @param clazz 查询数据类型
	 * @param columnNames 关心数据列名称，不传值时查询所有数据列
	 * @return 返回类型数据中所有条目，数据不重复显示。
	 */
	public <T> List<T> query(Class<T> clazz, String...columnNames) {
		return query(clazz, true, columnNames);
	}
	
	/**
	 * @param obj 查询数据类型及筛选值限定
	 * @param distinct 过滤重复数据
	 * @param selectionColumns 筛选数据列限定，不传值使用所有UNIQUE、PRIMARY、EQUALS标记列筛选
	 * @return 返回查询到的数据
	 */
	public <T> List<T> query(@NonNull T obj, boolean distinct, String...selectionColumns) {
		synchronized (this) {
			List<T> list = null;
			Class<T> clazz = (Class<T>) obj.getClass();
			creatTable(clazz);
			Table table = new Table(clazz);
			SQLDirect d = SQLDirectUtils.queryDirect(table, obj, selectionColumns, distinct, null, null, null, null, null, null);
			Cursor c = readDatabase(d);
			list = getDataList(table, clazz, c);
			return list;
		}
	}
	
	/**
	 * @param obj 查询数据类型及筛选值限定
	 * @param selectionColumns 筛选数据列限定，不传值使用所有UNIQUE、PRIMARY、EQUALS标记列筛选
	 * @return 返回查询到的数据,过滤重复数据
	 */
	public <T> List<T> query(@NonNull T select, String...selectionColumns) {
		return query(select, true, selectionColumns);
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
	public  <T> List<T> query(@NonNull Object select, String[] columnNames, boolean distinct, String [] groupByColumns, 
			String having, String [] orderByColumns, String orderBy, String limit, String offset) {
		synchronized (this) {
			List<T> list = null;
			Class<T> clazz = (Class<T>) select.getClass();
			creatTable(clazz);
			Table table = new Table(clazz);
			SQLDirect d = SQLDirectUtils.queryDirect(table, select, columnNames, distinct, groupByColumns, having, orderByColumns, orderBy, limit, offset);
			Cursor c = readDatabase(d);
			list = getDataList(table, clazz, c);
			return list;
		}
	}
	
	/**
	 * @param clazz 数据类型限定
	 * @param columnNames 关心数据列，null时查询所有数据列
	 * @param distinct 查询结果不重复限制
	 * @param selection where语句，值占位使用“？”
	 * @param selectArgs 替换占位符的值
	 * @param groupByColumns 语句用于结合合计函数，根据一个或多个列对结果集进行分组
	 * @param having 统计条件限制
	 * @param orderByColumns 排序限制条件
	 * @param orderBy 语句用于对结果集进行排序
	 * @param limit LIMIT 子句用于限制由 SELECT 语句返回的数据量
	 * @param offset offset limit的条目限制
	 */
	public  <T> List<T> query(@NonNull Class<T> clazz, String[] columnNames, boolean distinct, String selection, String[] selectArgs, 
			String [] groupByColumns, String having, String [] orderByColumns, String orderBy, String limit, String offset) {
		synchronized (this) {
			List<T> list = null;
			creatTable(clazz);
			Table table = new Table(clazz);
			SQLDirect d = SQLDirectUtils.queryDirect(table, columnNames, distinct, selection, selectArgs, groupByColumns, having, orderByColumns, orderBy, limit, offset);
			Cursor c = readDatabase(d);
			list = getDataList(table, clazz, c);
			return list;
		}
	}
	
	<T> List<T> getDataList(Table table, Class<T> type, final Cursor c, String...columns) {
		if(c == null || c.getCount() == 0) return null;
		List<T>  list = new ArrayList<T>();
		boolean filter = columns != null  && columns.length > 0;
		if(c.moveToFirst()) {
			HashMap<String, Column> entries = null;
			if(filter) {
				entries = new HashMap<String, Column>();
				for(String name : columns) {
					final Column cl = table.mColumns.get(name);
					if(cl != null) {
						entries.put(name, cl);
					}
				}
			} else {
				entries = new HashMap<String, Column>(table.mColumns);
			}
			List<Column> mColumns = new ArrayList<Column>(entries.values());
			
			try {
				do{
					Object obj = type.newInstance();
					for(Column col : mColumns) {
						col.setValueForObj(obj, c);
					}
					if(!list.contains(obj)) {
						T t = (T)obj;
						list.add(t);
					}
				} while(c.moveToNext());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	void writeDatabase(@NonNull SQLDirect d) {
		Log.e("db", "  SQL : " + d.toString());
		d.setSQLiteDatabase(mDbHelper.getWritableDatabase());
		d.beginTransaction();
		d.execSQL();
		d.setTransactionSuccessful();
		d.endTransaction();
		d.close();
	}
	
	Cursor readDatabase(@NonNull SQLDirect d) {
		Log.e("db", "  SQL : " + d.toString());
		d.setSQLiteDatabase(mDbHelper.getReadableDatabase());
		return d.rawQuery();
	}
}
