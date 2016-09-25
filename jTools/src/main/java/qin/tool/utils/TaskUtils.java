/**
 * 工程名:JTools
 * 文件名:TaskUtils.java
 * 包   名:qin.tool.utils
 * 日   期:2016年9月21日下午4:22:12
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Looper;
import android.support.annotation.NonNull;

/**
 *类   名:TaskUtils
 *功   能:TODO
 *
 *日  期:2016年9月21日 下午4:22:12
 * @author JeoyQin
 *
 */
public class TaskUtils {
	Looper mLooper = null;
	/*public */TaskUtils() {
	}

	/*public TaskUtils getUtils() {
	}*/

	public static <T> void asyncExec(@NonNull final Task<T> task) {
		new AsyncTask<Void	, Void, Void>() {
			@Override
			protected Void doInBackground(Void... params) {
				task.run();
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				task.finish(task.data);
			}
		}.execute();
	}

	public static abstract class Task  <T>  {
		protected T data = null;
		public abstract void run();
		public void finish(T data) {}
	}
}
