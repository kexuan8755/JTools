/**
 * 工程名:JTools
 * 文件名:FileUtils.java
 * 包   名:qin.tool.utils
 * 日   期:2016年9月21日上午10:26:42
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;

/**
 *类   名:FileUtils
 *功   能:TODO
 *
 *日  期:2016年9月21日 上午10:26:42
 * @author JeoyQin
 *
 */
public class FileUtils {
	
	/*public*/ FileUtils() {
	}
	
	public static String readStringFromStream(@NonNull InputStream is, String coder) {
		StringBuffer buffer = new StringBuffer();
		byte[] buff = new byte[1024];
		try {
			while(is.read(buff) > 0) {
				if(StringUtils.isEmpty(coder)) {
					buffer.append(new String(buff));
				} else {
					buffer.append(new String(buff, coder));
				}
			}
		} catch(Exception e) {
		}
		return buffer.toString();
	}
	
	public static String readStringFromAssets(@NonNull Context context, String fileName, String coder) {
		Resources res = context.getResources();
		InputStream is = res.openRawResource(res.getIdentifier(fileName, "raw", context.getPackageName()));
		return readStringFromStream(is, coder);
	}
	
	public static String readStringFromAssets(@NonNull Context context, int file, String coder) {
		Resources res = context.getResources();
		InputStream is = res.openRawResource(file);
		return readStringFromStream(is, coder);
	}
}
