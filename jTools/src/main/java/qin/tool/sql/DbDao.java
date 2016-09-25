/**
 * 工程名:JTools
 * 文件名:DbDao.java
 * 包   名:qin.tool.sql
 * 日   期:2016年8月17日上午9:12:53
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.sql;

import qin.tool.utils.StringUtils;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 *类   名:DbDao
 *功   能:TODO
 *
 *日  期:2016年8月17日 上午9:12:53
 * @author JeoyQin
 *
 */
public class DbDao {
	String name;
	int version;
	OnDbUpdateListener mListener;
	public Context mContext;
	
	public interface OnDbUpdateListener {
		public void onUpgrade(SQLiteDatabase database, int oldver, int newver);
	}
	
	public DbDao(Context context, OnDbUpdateListener l) {
		this(context.getApplicationContext(), null, 1, l);
	}
	
	public DbDao(Context context) {
		this(context, null, 1);
	}
	
	public DbDao(Context context, String name, int ver) {
		this(context, name, ver, null);
	}
	
	public DbDao(Context context, String name, int ver, OnDbUpdateListener listener) {
		this.mContext = context.getApplicationContext();
		this.name = name;
		this.version = ver;
		this.mListener = listener;
		if(StringUtils.isEmpty(this.name)) {
			final String pkg = mContext.getPackageName();
			this.name = pkg.replace(".", "_");
		}
	}
	
	public void setOnDbUpdateListener(OnDbUpdateListener mListener) {
		this.mListener = mListener;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o != null && o instanceof DbDao) {
			DbDao other = (DbDao) o;
			return StringUtils.equals(other.name, name) /*&& other.version == version*/;
		}
		return super.equals(o);
	}
	
	/**
	 * @return the mContext
	 */
	public Context getContext() {
		return mContext;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}
}
