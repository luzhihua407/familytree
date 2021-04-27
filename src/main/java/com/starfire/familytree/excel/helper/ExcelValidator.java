package com.starfire.familytree.excel.helper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义Excel检验注解
 * @author luzh
 * @version 1.0.0
 * @ClassName ExcelValidator.java
 * @Description TODO
 * @createTime 2021年03月30日 10:35:00
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ExcelValidator {

	/**
	 * 对应列索引 索引基零
	 * @return
	 */
	public int index();
	/**
	 * 不为空
	 * @return
	 */
	public boolean notEmpty() default false;

	/**
	 * 检验正则表达式
	 * @return
	 */
	public String regexp() default "";

	/**
	 * 错误信息
	 * @return
	 */
	public String error() default "";
}
