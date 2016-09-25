/**
 * 工程名:JTools
 * 文件名:StringValue.java
 * 包   名:qin.tool.value
 * 日   期:2016年8月16日下午7:38:07
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.value;

import android.database.Cursor;

/**
 *类   名:StringValue
 *功   能:TODO
 *
 *日  期:2016年8月16日 下午7:38:07
 * @author JeoyQin
 *
 */
public class StringValue extends Value<String> {

	@Override
	public qin.tool.value.Value.Type getType() {
		return Type.TEXT;
	}

	@Override
	public String getValue(Cursor c, int index) {
		String value = null;
		if(c != null) {
			int column = c.getColumnIndex(c.getColumnName(index));
			value = c.getString(column);
		}
		return value;
	}

	@Override
	public String toValueName(Object object, String def) {
		String value = "'" + def + "'";
		if(object != null) {
			value = "'" + object.toString() + "'";
		}
		return value;
	}

	@Override
	public String toValue(String valueName) {
		return valueName != null ? valueName : "";
	}
}
