/**
 * 工程名:JTools
 * 文件名:JTools.java
 * 包   名:qin.tool.utils
 * 日   期:2016年9月21日上午10:00:13
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.utils;

import android.support.annotation.NonNull;

/**
 *类   名:JTools
 *功   能:TODO
 *
 *日  期:2016年9月21日 上午10:00:13
 * @author JeoyQin
 *
 */
public class JArrayUtils {
	
	/*public*/ JArrayUtils() {
	}
	
	public static <T> boolean checkArrayIndex(T[] array, int index) {
		return array != null && array.length > index;
	}
	
	public static <T> int indexFromArray(T[] array, @NonNull T obj) {
		int index = -1;
		final int count = array != null ? array.length  : 0;
		for(int i = 0; i < count; i++) {
			if(array[1].equals(obj) || array[i] == obj) {
				index = i;
				break;
			}
		}
		return index;
	}
}
