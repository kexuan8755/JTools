/**
 * 工程名:JTools
 * 文件名:ViewUtils.java
 * 包   名:qin.tool.utils
 * 日   期:2016年8月16日上午10:32:19
 * 作   者:fyt 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 *类   名:ViewUtils
 *功   能:TODO
 *
 *日  期:2016年8月16日 上午10:32:19
 * @author fyt
 *
 */
public class ViewUtils {
	
	public static View findViewById(View layout, int vid)  {
		View v = null;
		if(layout != null) {
			v = layout.findViewById(vid);
		}
		return v;
	}
	
	public static void setViewBackground(View layout, int vid, Drawable drawable) {
		View v = findViewById(layout, vid);
		if(v != null)
			v.setBackground(drawable);
	}
	
	public static void setViewBackground(View layout, int vid, int drawable) {
		View v = findViewById(layout, vid);
		if(v != null)
			v.setBackgroundResource(drawable);
	}
	
	public static void setVisibility(View layout, int vid, int visibility) {
		View v = findViewById(layout, vid);
		if(v != null && v.getVisibility() != visibility)
			v.setVisibility(visibility);
	}
	
	public static void setText(View layout, int vid, String text) {
		TextView tv = null;
		try{
			tv = (TextView) findViewById(layout, vid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(tv != null) 
			tv.setText(text);
	}
	
	public static void setText(View layout, int vid, int text) {
		TextView tv = null;
		try{
			tv = (TextView) findViewById(layout, vid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(tv != null) 
			tv.setText(text);
	}
	
	public static void setImageDrawable(View layout, int vid, Drawable drawable) {
		ImageView iv = null;
		try{
			iv = (ImageView) findViewById(layout, vid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(iv != null) 
			iv.setImageDrawable(drawable);
	}
	
	public static void setImageResource(View layout, int vid, int drawable) {
		ImageView iv = null;
		try{
			iv = (ImageView) findViewById(layout, vid);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(iv != null) 
			iv.setImageResource(drawable);
	}
	
	public static void setOnClickListener(View layout, int vid, OnClickListener listener) {
		View v = findViewById(layout, vid);
		if(v != null)
			v.setOnClickListener(listener);
	}
	
	public static void setOnLongClickListener(View layout, int vid, OnLongClickListener listener) {
		View v = findViewById(layout, vid);
		if(v != null)
			v.setOnLongClickListener(listener);
	}
}
