/**
 * 工程名:JTools
 * 文件名:DatabaseHelper.java
 * 包   名:qin.tool.sql
 * 日   期:2016年8月17日上午9:09:05
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

/**
 *类   名:DatabaseHelper
 *功   能:TODO
 *
 *日  期:2016年8月17日 上午9:09:05
 * @author JeoyQin
 *
 */
public class DbHelper extends SQLiteOpenHelper {
	DbDao dao;
	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 * @param errorHandler
	 */
	public DbHelper(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, name, factory, version, errorHandler);
	}

	/**
	 * @param context
	 * @param name
	 * @param factory
	 * @param version
	 */
	public DbHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}
	
	public DbHelper(DbDao dao) {
		this(dao.mContext, dao.name, null, dao.version);
		this.dao = dao;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(dao != null && dao.mListener != null) {
			dao.mListener.onUpgrade(db, oldVersion, newVersion);
		}
	}

	public long insert(String table, ContentValues values) {
		SQLiteDatabase mDatabase = getWritableDatabase();
		long result = mDatabase.insert(table, null, values);
		mDatabase.close();
		return result;
	}
	
	public long replace(String table, ContentValues values) {
		SQLiteDatabase mDatabase = getWritableDatabase();
		long result = mDatabase.replace(table, null, values);
		mDatabase.close();
		return result;
	}
	
	public int delete(String table, String whereClause, String[] whereArgs) {
		SQLiteDatabase mDatabase = getWritableDatabase();
		int result = mDatabase.delete(table, whereClause, whereArgs);
		mDatabase.close();
		return result;
	}
	
	public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
		SQLiteDatabase mDatabase = getWritableDatabase();
		int result = mDatabase.update(table, values, whereClause, whereArgs);
		mDatabase.close();
		return result;
	}
	
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs) {
		SQLiteDatabase mDatabase = getReadableDatabase();
		return mDatabase.query(table, columns, selection, selectionArgs, null, null, null);
	}
}
