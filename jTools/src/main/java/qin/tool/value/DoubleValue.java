/**
 * 工程名:JTools
 * 文件名:DoubleValue.java
 * 包   名:qin.tool.value
 * 日   期:2016年8月16日下午8:06:33
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.value;

import qin.tool.utils.StringUtils;
import qin.tool.value.Value.Type;
import android.database.Cursor;

/**
 *类   名:DoubleValue
 *功   能:TODO
 *
 *日  期:2016年8月16日 下午8:06:33
 * @author JeoyQin
 *
 */
public class DoubleValue extends Value<Double> {
	@Override
	public qin.tool.value.Value.Type getType() {
		return Type.REAL;
	}

	@Override
	public Double getValue(Cursor c, int index) {
		Double value = null;
		if(c != null) {
			int column = c.getColumnIndex(c.getColumnName(index));
			value = c.getDouble(column);
		}
		return value;
	}

	@Override
	public String toValueName(Object object, String def) {
		String value = def;
		if(object != null && object instanceof Double) {
			value = ((Double)object).toString();
		}
		return value;
	}

	@Override
	public Double toValue(String valueName) {
		Double value = new Double(0);
		if(!StringUtils.isEmpty(valueName)) {
			try {
				value = Double.valueOf(valueName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return value;
	}
}
