/**
 * 工程名:JTools
 * 文件名:Value.java
 * 包   名:qin.tool.value
 * 日   期:2016年8月16日下午7:09:47
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.value;

import android.database.Cursor;

/**
 *类   名:Value
 *功   能:TODO
 *
 *日  期:2016年8月16日 下午7:09:47
 * @author JeoyQin
 *
 */
public abstract class Value <T>{
	public enum Type {
		INTEGER("INTEGER"),
		REAL("REAL"),
		TEXT("TEXT"),
		BLOB("BLOB"),
		BOOLEAN("INTEGER"),
		DOUBLE("REAL"),
		DATE("TEXT");
		
		String value;
		
		private Type(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
	}
	
	public abstract Type getType();
	public abstract T getValue(Cursor c, int index);
	public abstract T toValue(String valueName);
	public abstract String toValueName(Object object, String def);
	public  Object getValue(T value) {
		return value;
	}
}
