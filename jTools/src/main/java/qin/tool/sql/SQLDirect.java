/**
 * 工程名:JTools
 * 文件名:Direct.java
 * 包   名:qin.tool.sql
 * 日   期:2016年8月17日上午11:40:28
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.sql;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;

import qin.tool.utils.StringUtils;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

/**
 *类   名:Direct
 *功   能:指令描述类
 *
 *日  期:2016年8月17日 上午11:40:28
 * @author JeoyQin
 *
 */
public class SQLDirect {
	String sql;
	LinkedList<Object> args = new LinkedList<Object>();
	LinkedList<String> argsNames = new LinkedList<String>();
	SQLiteDatabase mDatabase = null;
	
	public SQLDirect(String sql) {
		this.sql = sql;
	}

	public SQLDirect() {
	}
	
	/**
	 * @param sql the sql to set
	 */
	public void setCommand(String sql) {
		this.sql = sql;
	}
	
	public void push(Object arg) {
		args.add(arg);
	}
	
	public void pushName(String arg) {
		argsNames.add(arg);
	}
	
	/**
	 * @return the args
	 */
	public Object[] getArgs() {
		return args == null ? null : args.toArray();
	}
	
	public String[] getArgsNames() {
		final int length = argsNames.size();
		String [] args = length > 0 ? new String[length] : new String[]{};
		for(int i = 0; i < length; i++) {
			args[i] = argsNames.get(i);
		}
		return args;
	}
	
	/**
	 * @param mDatabase the mDatabase to set
	 */
	public void setSQLiteDatabase(SQLiteDatabase mDatabase) {
		this.mDatabase = mDatabase;
	}
	
	public Cursor rawQuery() {
		if(mDatabase != null && StringUtils.isEmpty(sql)) return null;

		return mDatabase.rawQuery(sql, getArgsNames());
	}
	
	public void execSQL() {
		if(mDatabase != null && StringUtils.isEmpty(sql)) return;
		mDatabase.execSQL(sql, getArgs());
	}
	
	public void close() {
		if(mDatabase != null) {
			mDatabase.close();
		}
	}
	
	public void beginTransaction() {
		if(mDatabase != null) mDatabase.beginTransaction();
	}
	
	public void setTransactionSuccessful() {
		if(mDatabase != null) {
			mDatabase.setTransactionSuccessful();
		}
	}
	
	public void endTransaction() {
		if(mDatabase != null) {
			mDatabase.endTransaction();
		}
	}
	
	public void clear() {
		sql = null;
		args.clear();
		argsNames.clear();
	}
	
	@Override
	public String toString() {
		String msg = sql;
		if(args != null && !args.isEmpty()) {
			msg += "\nargs : " + Arrays.toString(args.toArray());
		}
		if(argsNames != null && !argsNames.isEmpty()) {
			msg += "\nargsNames : " + Arrays.toString(argsNames.toArray());
		}
		return msg;
	}
}
