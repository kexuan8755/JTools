/**
 * 工程名:JTools
 * 文件名:Column.java
 * 包   名:qin.tool.sql
 * 日   期:2016年8月16日下午7:08:21
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.sql;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;

import android.database.Cursor;
import qin.tool.annotation.Column.ColumnType;
import qin.tool.utils.ClazzUtils;
import qin.tool.utils.StringUtils;
import qin.tool.value.BlobValue;
import qin.tool.value.BooleanValue;
import qin.tool.value.DoubleValue;
import qin.tool.value.FloatValue;
import qin.tool.value.IntValue;
import qin.tool.value.StringValue;
import qin.tool.value.Value;
import qin.tool.value.Value.Type;

/**
 * 类 名:Column 功 能:数据库列描述，数据库的一列数据信息，主要关心的是数据列的命名，
 * 保存的数据类型。以及该列的unique、primary、foreign的属性的限制
 *
 * 日 期:2016年8月16日 下午7:08:21
 * 
 * @author JeoyQin
 *
 */
public class Column {
	/**
	 * 数据库工具支持的所有数据类型
	 */
	static HashMap<String, qin.tool.value.Value<?>> mValus = null;
	static {
		mValus = new HashMap<String, qin.tool.value.Value<?>>();
		qin.tool.value.Value<?> value = new IntValue();
		mValus.put(int.class.getName(), value);
		mValus.put(Integer.class.getName(), value);

		value = new StringValue();
		mValus.put(String.class.getName(), value);

		value = new FloatValue();
		mValus.put(float.class.getName(), value);
		mValus.put(Float.class.getName(), value);

		value = new DoubleValue();
		mValus.put(double.class.getName(), value);
		mValus.put(Double.class.getName(), value);

		value = new BooleanValue();
		mValus.put(boolean.class.getName(), value);
		mValus.put(Boolean.class.getName(), value);

		value = new BlobValue();
		mValus.put(byte[].class.getName(), value);
	}

	int index = -1;
	String name;
	Object value;
	Object defValue;
	TYPE type = TYPE.NULL;
	// 与该列数据关联的变量
	Field mField;
	Method geter;
	Method seter;
	Value<?> valueEntry;
	boolean autoIncrement = false;
	boolean notNull = false;
	
	public enum TYPE {
		UNIQUE("UNIQUE"), PRIMARY("PRIMARY KEY"), FOREIGN("FOREIGN KEY"), EQUALS(
				""), COLUMN(""), NULL("");
		String value;

		private TYPE(String value) {
			this.value = value;
		}

		/**
		 * @return the value
		 */
		public String getValue() {
			return value;
		}
	}

	public Column(Class<?> clazz, Field field, TYPE type) {
		mField = field;
		valueEntry = getValueTpye(mField);
		this.type = type;
		switch (this.type) {
		case FOREIGN:
		case UNIQUE:
		case EQUALS:
		case COLUMN:
		case PRIMARY: {
			qin.tool.annotation.Column c = ClazzUtils.getAnnotation(field,
					qin.tool.annotation.Column.class);
			if (c != null) {
				name = c.name();
				final String def = c.defValue();
				if (!StringUtils.isEmpty(def)) {
					defValue = valueEntry.toValue(def);
				}
				notNull = c.notNull();
				autoIncrement = c.autoIncrement();
				findGeter(clazz, c.geterName());
				findSeter(clazz, c.seterName());
				if(StringUtils.isEmpty(name)) {
					name = mField.getName();
				}
			}
			break;
		}
		default:
			break;
		}
	}

	/**
	 * 搜集对应属性的Geter方法，方便数据库工具操作数据库时提取对应属性值
	 */
	void findGeter(Class<?> clazz, String method) {
		Method func = ClazzUtils.findMethod(clazz, method);
		if (func != null) {
			geter = func;
			return;
		}

		final String fieldName = mField.getName();
		if (!StringUtils.isEmpty(fieldName)) {
			final Class<?> type = mField.getType();
			String methodName = null;
			if (fieldName.startsWith("is")) {
				methodName = fieldName;
			} else if (boolean.class.equals(type) || Boolean.class.equals(type)) {
				methodName = "is"
						+ fieldName.substring(0, 1).toUpperCase(Locale.US)
						+ fieldName.substring(1);
			} else {
				methodName = "get"
						+ fieldName.substring(0, 1).toUpperCase(Locale.US)
						+ fieldName.substring(1);
			}

			if (!StringUtils.isEmpty(methodName)) {
				geter = ClazzUtils.findMethod(clazz, methodName);
			}
		}
	}

	public Value.Type getValueType() {
		return valueEntry == null ? Type.TEXT : valueEntry.getType();
	}
	
	/**
	 * 搜集对应属性的Geter方法，方便数据库工具提取数据库数据时还原对应属性值
	 */
	void findSeter(Class<?> clazz, String method) {
		Method func = ClazzUtils.findMethod(clazz, method);
		if (func != null) {
			seter = func;
			return;
		}

		final String fieldName = mField.getName();
		if (!StringUtils.isEmpty(fieldName)) {
			String methodName = null;
			if (fieldName.startsWith("is")) {
				methodName = "set"
						+ fieldName.substring(2, 3).toUpperCase(Locale.US)
						+ fieldName.substring(3);
			} else {
				methodName = "set"
						+ fieldName.substring(0, 1).toUpperCase(Locale.US)
						+ fieldName.substring(1);
			}

			if (!StringUtils.isEmpty(methodName)) {
				geter = ClazzUtils.findMethod(clazz, methodName);
			}
		}
	}


	public Object getValueFromObj(Object obj) {
		Object result = null;
		if(geter != null && obj != null) {
			result = ClazzUtils.callMethod(obj, geter);
		}
		
		if(result == null && obj != null && mField != null) {
			mField.setAccessible(true);
			try {
				result = mField.get(obj);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} finally {
//				mField.setAccessible(false);
			}
		}
		return result;
	}
	
	public void setValueForObj(Object obj, Cursor c)  {
		if(index < 0) {
			index = c.getColumnIndex(name);
		}
		
		if(index >= 0 && valueEntry != null) {
			setValueForObj(obj, valueEntry.getValue(c, index));
		}
	}
	
	public void setValueForObj(Object obj, Object value) {
		if(obj == null) return;
		boolean success = false;
		if(mField != null) {
			mField.setAccessible(true);
			try {
				mField.set(obj, value);
				success = true;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} finally {
//				mField.setAccessible(false);
			}
		}
		
		if(!success && seter != null && obj != null) {
			ClazzUtils.callMethod(obj, seter, value);
		}
	}
	
	public static qin.tool.value.Value<?> getValueTpye(Field field) {
		qin.tool.value.Value<?> value = null;
		if (field != null) {
			final Class<?> clazz = field.getType();
			final String name = clazz.getName();
			if (valueSupport(name)) {
				value = mValus.get(name);
			}
		}
		return value;
	}

	public static boolean valueSupport(String name) {
		boolean result = false;
		if (mValus.containsKey(name)) {
			result = true;
		} else {

		}
		return result;
	}

	public static TYPE getColumnType(Field field) {
		TYPE type = TYPE.NULL;

		if (type == TYPE.NULL) {
			qin.tool.annotation.Column c = ClazzUtils.getAnnotation(field,
					qin.tool.annotation.Column.class);
			if (c != null) {
				final ColumnType ct = c.type();
				switch (ct) {
				case COLUMN:
					type = TYPE.COLUMN;
					break;
				case PRIMARY:
					type = TYPE.PRIMARY;
					break;
				case UNIQUE:
					type = TYPE.UNIQUE;
					break;
				case EQUALS:
					type = TYPE.EQUALS;
					break;
				default:
					type = TYPE.COLUMN;
					break;
				}
			}
		}
		return type;
	}
}
