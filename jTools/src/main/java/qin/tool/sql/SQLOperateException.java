/**
 * 工程名:JTools
 * 文件名:SQLOperateException.java
 * 包   名:qin.tool.sql
 * 日   期:2016年9月12日下午7:53:19
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.sql;

/**
 *类   名:SQLOperateException
 *功   能:TODO
 *
 *日  期:2016年9月12日 下午7:53:19
 * @author JeoyQin
 *
 */
public class SQLOperateException extends RuntimeException {

	/**
	 * serialVersionUID(描述这个变量表示什么).
	 */
	private static final long serialVersionUID = -8363566018699258471L;

	/**
	 * 
	 */
	public SQLOperateException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param detailMessage
	 * @param throwable
	 */
	public SQLOperateException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param detailMessage
	 */
	public SQLOperateException(String detailMessage) {
		super(detailMessage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param throwable
	 */
	public SQLOperateException(Throwable throwable) {
		super(throwable);
		// TODO Auto-generated constructor stub
	}

}
