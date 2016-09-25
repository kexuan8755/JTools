/**
 * 工程名:JTools
 * 文件名:JSONUtils.java
 * 包   名:qin.tool.utils
 * 日   期:2016年8月16日上午10:01:44
 * 作   者:fyt 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *类   名:JSONUtils
 *功   能:TODO
 *
 *日  期:2016年8月16日 上午10:01:44
 * @author fyt
 *
 */
public class JSONUtils {
	
	public static JSONObject getJSONObject(JSONObject json, String name) {
		JSONObject result = null;
		if(json != null && !StringUtils.isEmpty(name) && json.has(name)) {
			try {
				result = json.getJSONObject(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static JSONObject getJSONObjectFromArray(JSONObject json, String name, int index) {
		JSONObject result = null;
		JSONArray array = getJSONArray(json, name);
		final int count = array == null ? 0 : array.length();
		if(index >= 0 && index < count) {
			try {
				result = array.getJSONObject(index);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static JSONArray getJSONArray(JSONObject json, String name) {
		JSONArray result = null;
		if(json != null && !StringUtils.isEmpty(name) && json.has(name)) {
			try {
				result = json.getJSONArray(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static JSONArray getJSONArrayForArray(JSONObject json, String name, int index) {
		JSONArray result = null;
		JSONArray array = getJSONArray(json, name);
		final int count = array == null ? 0 : array.length();
		if(index >= 0 && index < count) {
			try {
				result = array.getJSONArray(index);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static int getInt(JSONObject json, String name, int def) {
		int result = def;
		JSONObject entry = getJSONObject(json, name);
		if(entry != null) {
			try {
				result = entry.getInt(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static int[] getIntArray(JSONObject json, String name) {
		int[] result = null;
		JSONArray array = getJSONArray(json, name);
		final int count = array == null ? 0 : array.length();
		if(count > 0) {
			result = new int[count];
			for(int index = 0; index < count; index++) {
				try {
					result[index]= array.getInt(index);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static String getString(JSONObject json, String name, String def) {
		String result = def;
		JSONObject entry = getJSONObject(json, name);
		if(entry != null) {
			try {
				result = entry.getString(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static String[] getStringArray(JSONObject json, String name) {
		String[] result = null;
		JSONArray array = getJSONArray(json, name);
		final int count = array == null ? 0 : array.length();
		if(count > 0) {
			result = new String[count];
			for(int index = 0; index < count; index++) {
				try {
					result[index]= array.getString(index);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static double getDouble(JSONObject json, String name, double def) {
		double result = def;
		JSONObject entry = getJSONObject(json, name);
		if(entry != null) {
			try {
				result = entry.getDouble(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static double[] getDoubleArray(JSONObject json, String name) {
		double[] result = null;
		JSONArray array = getJSONArray(json, name);
		final int count = array == null ? 0 : array.length();
		if(count > 0) {
			result = new double[count];
			for(int index = 0; index < count; index++) {
				try {
					result[index]= array.getDouble(index);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static long getLong(JSONObject json, String name, long def) {
		long result = def;
		JSONObject entry = getJSONObject(json, name);
		if(entry != null) {
			try {
				result = entry.getLong(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static long[] getLongArray(JSONObject json, String name) {
		long[] result = null;
		JSONArray array = getJSONArray(json, name);
		final int count = array == null ? 0 : array.length();
		if(count > 0) {
			result = new long[count];
			for(int index = 0; index < count; index++) {
				try {
					result[index]= array.getLong(index);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	public static boolean getBoolean(JSONObject json, String name, boolean def) {
		boolean result = def;
		JSONObject entry = getJSONObject(json, name);
		if(entry != null) {
			try {
				result = entry.getBoolean(name);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static boolean[] getBooleanArray(JSONObject json, String name) {
		boolean[] result = null;
		JSONArray array = getJSONArray(json, name);
		final int count = array == null ? 0 : array.length();
		if(count > 0) {
			result = new boolean[count];
			for(int index = 0; index < count; index++) {
				try {
					result[index]= array.getBoolean(index);
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
}
