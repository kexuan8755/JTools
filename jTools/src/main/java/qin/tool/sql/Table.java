/**
 * 工程名:JTools
 * 文件名:Table.java
 * 包   名:qin.tool.sql
 * 日   期:2016年8月17日下午4:53:37
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.sql;

import android.support.annotation.NonNull;

import java.lang.reflect.Field;
import java.util.HashMap;

import qin.tool.sql.Column.TYPE;
import qin.tool.utils.ClazzUtils;
import qin.tool.utils.StringUtils;

/**
 *类   名:Table
 *功   能:TODO
 *
 *日  期:2016年8月17日 下午4:53:37
 * @author JeoyQin
 *
 */
public class Table {
	String name;
	boolean exsit = false;
	boolean hasPrimary = false;
	boolean hasUnique = false;
	HashMap<String, Column> mColumns = new HashMap<String, Column>();
	
	public Table(@NonNull Class<?> clazz) {
		initName(clazz);
		initColums(clazz);
	}
	
	void initName(Class<?> clazz) {
		String name = null;
		qin.tool.annotation.Table table = ClazzUtils.getAnnotation(clazz, qin.tool.annotation.Table.class);
		if(table != null) {
			name = table.name();
		}
		
		if(StringUtils.isEmpty(name)) {
			name = clazz.getName();
			if(!StringUtils.isEmpty(name)) {
				this.name = name.replace(".", "_");
			}
		}
	}
	
	void initColums(Class<?> clazz) {
		Field [] fields = ClazzUtils.findFields(clazz);
		for(Field field : fields) {
			TYPE type = Column.getColumnType(field);
			if(type != TYPE.NULL) {
				Column c = new Column(clazz, field, type);
				//保证数据表只有一个主键值
				if(c.type == TYPE.PRIMARY && !hasPrimary) {
					hasPrimary = true;
				} else if(c.type == TYPE.PRIMARY) {
					c.type = TYPE.COLUMN;
				} if(c.type == TYPE.UNIQUE && !hasUnique) {
					hasUnique = true;
				}
				
				if(!mColumns.containsKey(c.name))
					mColumns.put(c.name, c);
			}
		}
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof Table) {
			Table other = (Table) o;
			if(!StringUtils.isEmpty(name)) 
				return StringUtils.equals(name, other.name); 
		}
		return super.equals(o);
	}
}
