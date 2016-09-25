/**
 * 工程名:JTools
 * 文件名:Colume.java
 * 包   名:qin.tool.annotation
 * 日   期:2016年8月16日下午3:59:32
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *类   名:Colume
 *功   能:TODO
 *
 * FOREIGN 一个表中的 FOREIGN KEY 指向另一个表中的 PRIMARY KEY
 *  CREATE TABLE Orders
 *  (
 *  Id_O int NOT NULL,
 *  OrderNo int NOT NULL,
 *  Id_P int,
 *  PRIMARY KEY (Id_O),
 *  FOREIGN KEY (Id_P) REFERENCES Persons(Id_P)
 *  )
 *  
 * UNIQUE数据库唯一记录限制
 * UNIQUE 约束唯一标识数据库表中的每条记录。
 * UNIQUE 和 PRIMARY KEY 约束均为列或列集合提供了唯一性的保证。
 * PRIMARY KEY 拥有自动定义的 UNIQUE 约束。
 * 每个表可以有多个 UNIQUE 约束，但是每个表只能有一个 PRIMARY KEY 约束。
 *  CREATE TABLE Persons
 *  (
 *  Id_P int NOT NULL,
 *  LastName varchar(255) NOT NULL,
 *  FirstName varchar(255),
 *  Address varchar(255),
 *  City varchar(255),
 *  UNIQUE (Id_P)
 *  )
 *  
 *  EQUALS 对象可能有很多属性需要记录，但是对比时我们关心的没有那么多，
 *  可以用此注解记录默认的比对数据，无此记录默认为所有数据列对比。
 *日  期:2016年8月16日 下午3:59:32
 * @author JeoyQin
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Inherited
public @interface Column {
	public enum ColumnType {
		COLUMN,//普通数据列
		PRIMARY,//主键值
		UNIQUE, //数据列唯一限制
		FOREIGN,//外健限制
		EQUALS   //对象相等默认的比较数据
	}
	public String name() default "";
	public String defValue() default "";
	public ColumnType type() default ColumnType.COLUMN;
	public boolean notNull() default false;
	//默认主键值不自动递增
	public boolean autoIncrement() default false;
	public String seterName() default "";
	public String geterName() default "";
}
