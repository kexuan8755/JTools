/**
 * 工程名:JTools
 * 文件名:IntValue.java
 * 包   名:qin.tool.value
 * 日   期:2016年8月16日下午7:24:47
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.value;

import java.lang.reflect.Field;

import qin.tool.utils.StringUtils;
import android.database.Cursor;

/**
 *类   名:IntValue
 *功   能:TODO
 *
 *日  期:2016年8月16日 下午7:24:47
 * @author JeoyQin
 *
 */
public class IntValue extends Value<Integer> {

	@Override
	public qin.tool.value.Value.Type getType() {
		return Type.INTEGER;
	}

	@Override
	public Integer getValue(Cursor c, int index) {
		Integer value = null;
		if(c != null) {
			int column = c.getColumnIndex(c.getColumnName(index));
			value = c.getInt(column);
		}
		return value;
	}

	@Override
	public String toValueName(Object object, String def) {
		String value = def;
		if(object != null && object instanceof Integer) {
			value = ((Integer)object).toString();
		}
		return value;
	}

	@Override
	public Integer toValue(String valueName) {
		Integer value = new Integer(0);
		if(!StringUtils.isEmpty(valueName)) {
			try{
				value = Integer.valueOf(valueName);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return value;
	}
}
