/**
 * 工程名:JTools
 * 文件名:Table.java
 * 包   名:qin.tool.annotation
 * 日   期:2016年8月16日下午3:54:57
 * 作   者:JeoyQin 
 * Copyright (c) 2016, kexuan52@yeah.net All Rights Reserved.
 *
 */
package qin.tool.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *类   名:Table
 *功   能:TODO
 *
 *日  期:2016年8月16日 下午3:54:57
 * @author JeoyQin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Table {
	public String name() default "";
}
