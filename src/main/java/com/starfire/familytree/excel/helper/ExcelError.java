package com.starfire.familytree.excel.helper;

import lombok.Data;

import java.io.Serializable;

/**
 * Excel解析错误类
 * @author luzh
 * @version 1.0.0
 * @ClassName ExcelError.java
 * @Description TODO
 * @createTime 2021年03月30日 09:54:00
 */
@Data
public class ExcelError implements Serializable {

	/**
	 * Excel行索引 0开始
	 */
	private Integer index;

	/**
	 * 属性名称
	 */
	private String property;

	/**
	 * 错误信息
	 */
	private String message;

	public ExcelError(){

	}

	public ExcelError(Integer index, String property, String message) {
		this.index = index;
		this.property = property;
		this.message = message;
	}
}
