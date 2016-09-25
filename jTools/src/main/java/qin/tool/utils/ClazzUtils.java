/**
 * 工程名:JTools
 * 文件名:ClazzUtils.java
 * 包   名:qin.tool.utils
 * 日   期:2016年8月16日上午9:12:31
 * 作   者:fyt 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *类   名:ClazzUtils
 *功   能:TODO
 *
 *日  期:2016年8月16日 上午9:12:31
 * @author fyt
 *
 */
public class ClazzUtils {
	
	private ClazzUtils() {
		throw new RuntimeException("NO ClazzUtils");
	}
	
	public static Method findMethod(String clazzName, String methodName, Class<?>...paramTypes) {
		Class<?> clazz = null;
		if(!StringUtils.isEmpty(clazzName) && !StringUtils.isEmpty(methodName)) {
			try {
				clazz = Class.forName(clazzName);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return findMethod(clazz, methodName, paramTypes);
	}
	
	public static Method findMethod(Class<?> clazz, String methodName, Class<?>...paramTypes) {
		Method method = null;
		if(clazz != null && !StringUtils.isEmpty(methodName)) {
			try {
				method = clazz.getMethod(methodName, paramTypes);
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return method;
	}
	
	public static Object callMethod(Object obj, Method method, Object...params) {
		Object result = null;
		if(method != null) {
			try {
				method.invoke(obj, params);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static Field[] findFields(Class<?> clazz) {
		Field [] result = null;
		if(clazz != null) {
			result = clazz.getDeclaredFields();
		}
		return result;
	}
	
	public static Field findFields(Class<?> clazz, String fieldName) {
		Field result = null;
		if(clazz != null) {
			try {
				result = clazz.getField(fieldName);
			} catch (NoSuchFieldException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String getFieldName(Field field) {
		if(field == null) return "";
		return field.getName();
	}
	
	public static <A extends Annotation> A getAnnotation (Field field, Class<A> clazz) {
		A result = null;
		if(field != null && clazz != null) {
			result = field.getAnnotation(clazz);
		}
		return result;
	}
	
	public static <A extends Annotation> A getAnnotation (Class<?> cla, Class<A> clazz) {
		A result = null;
		if(cla != null && clazz != null) {
			result = cla.getAnnotation(clazz);
		}
		return result;
	}
}
