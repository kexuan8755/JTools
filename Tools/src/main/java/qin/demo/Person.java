/**
 * 工程名:AutolibsDemo
 * 文件名:Person.java
 * 包   名:qin.demo
 * 日   期:2016年8月16日下午4:23:17
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.demo;

import qin.tool.annotation.Column;
import qin.tool.annotation.Column.ColumnType;
import qin.tool.annotation.Table;

/**
 *类   名:Person
 *功   能:TODO
 *
 *日  期:2016年8月16日 下午4:23:17
 * @author JeoyQin
 *
 */
@Table
public class Person {
	@Column(notNull=true)
	int id;
	@Column(type=ColumnType.UNIQUE)
	String name;
	@Column
	String number;
	public Person(){}
	public Person(int id, String name, String number){
		this.id = id;
		this.name = name;
		this.number = number;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}
	/**
	 * @param number the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "name : " + name + " id : " + id + " number : " + number;
	}
}
