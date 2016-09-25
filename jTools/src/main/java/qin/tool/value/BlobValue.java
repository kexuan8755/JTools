/**
 * 工程名:JTools
 * 文件名:BlobValue.java
 * 包   名:qin.tool.value
 * 日   期:2016年8月16日下午8:10:32
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.value;

import android.database.Cursor;

import qin.tool.utils.StringUtils;

/**
 *类   名:BlobValue
 *功   能:TODO
 *
 *日  期:2016年8月16日 下午8:10:32
 * @author JeoyQin
 *
 */
public class BlobValue extends Value<byte[]> {

	@Override
	public qin.tool.value.Value.Type getType() {
		return Type.BLOB;
	}

	@Override
	public byte[] getValue(Cursor c, int index) {
		byte [] value = null;
		if(c != null) {
			int column = c.getColumnIndex(c.getColumnName(index));
			value = c.getBlob(column);
		}
		return value;
	}

	@Override
	public String toValueName(Object object, String def) {
		String value = def;
		if(object != null && object instanceof byte[]) {
			value = ((byte[])object).toString();
		}
		return value;
	}

	@Override
	public byte[] toValue(String valueName) {
		byte[] value = new byte[0];
		if(!StringUtils.isEmpty(valueName)) {
			try{
				value = valueName.getBytes();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return value;
	}
}
