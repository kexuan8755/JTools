/**
 * 工程名:JTools
 * 文件名:FloatValue.java
 * 包   名:qin.tool.value
 * 日   期:2016年8月16日下午7:42:19
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.value;

import qin.tool.utils.StringUtils;
import android.database.Cursor;

/**
 *类   名:FloatValue
 *功   能:TODO
 *
 *日  期:2016年8月16日 下午7:42:19
 * @author JeoyQin
 *
 */
public class FloatValue extends Value<Float> {

	@Override
	public qin.tool.value.Value.Type getType() {
		return Type.REAL;
	}

	@Override
	public Float getValue(Cursor c, int index) {
		Float value = null;
		if(c != null) {
			int column = c.getColumnIndex(c.getColumnName(index));
			value = c.getFloat(column);
		}
		return value;
	}

	@Override
	public String toValueName(Object object, String def) {
		String value = def;
		if(object != null && object instanceof Float) {
			value = ((Float)object).toString();
		}
		return value;
	}

	@Override
	public Float toValue(String valueName) {
		Float value = new Float(0);
		if(!StringUtils.isEmpty(valueName)) {
			try {
				value = Float.valueOf(valueName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return value;
	}
}
