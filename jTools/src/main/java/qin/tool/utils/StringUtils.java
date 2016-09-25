/**
 * 工程名:JTools
 * 文件名:StringUtils.java
 * 包   名:qin.tool.utils
 * 日   期:2016年8月16日上午9:20:18
 * 作   者:fyt 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *类   名:StringUtils
 *功   能:TODO
 *
 *日  期:2016年8月16日 上午9:20:18
 * @author fyt
 *
 */
public class StringUtils {
	
	public static boolean isEmpty(String str) {
		return str == null || str.length() == 0;
	}
	
	public static boolean equals(String strl, String strr) {
		return strl == null ? strl == null : strl.equals(strr);
	}
	
	public static boolean findStringFromArray(String[] array, String str) {
		boolean found = false;
		if(array != null && str != null) {
			for(String value : array) {
				if(!found && equals(value, str)) {
					found = true;
					break;
				}
			}
		}
		return found;
	}
	
	/**
	 * 正则式匹配
	 * @param str 匹配字符串
	 * @param format 匹配格式
	 */
	public static boolean mathcher(String str, String format) {
		boolean result = false;
		Pattern pattern = Pattern.compile(format);
		if(!isEmpty(format)) {
			pattern = Pattern.compile(format);
		}
		
		if(pattern != null) {
			Matcher matcher = pattern.matcher(str);
			result = matcher == null ? false : matcher.matches();
		}
		return result;
	}
}
