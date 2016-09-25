/**
 * 工程名:JTools
 * 文件名:BooleanValue.java
 * 包   名:qin.tool.value
 * 日   期:2016年8月16日下午7:48:17
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.value;

import qin.tool.utils.StringUtils;
import android.database.Cursor;

/**
 *类   名:BooleanValue
 *功   能:TODO
 *
 *日  期:2016年8月16日 下午7:48:17
 * @author JeoyQin
 *
 */
public class BooleanValue extends Value<Boolean> {

	@Override
	public qin.tool.value.Value.Type getType() {
		return Type.BOOLEAN;
	}

	@Override
	public Boolean getValue(Cursor c, int index) {
		int value = 0;
		if(c != null) {
			int column = c.getColumnIndex(c.getColumnName(index));
			value = c.getInt(column);
		}
		return value == 1;
	}

	@Override
	public String toValueName(Object object, String def) {
		String value = def;
		if(object != null && object instanceof Boolean) {
			value = ((Boolean)object).toString();
		}
		return value;
	}

	@Override
	public Boolean toValue(String valueName) {
		Boolean value = new Boolean(false);
		if(!StringUtils.isEmpty(valueName)) {
			try{
				value = Boolean.valueOf(valueName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return value;
	}
}
