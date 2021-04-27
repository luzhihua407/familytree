package com.starfire.familytree.excel.helper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel解析结果类
 * @author luzh
 * @version 1.0.0
 * @ClassName ExcelResult.java
 * @Description TODO
 * @createTime 2021年03月30日 09:54:00
 */
public class ExcelResult<T extends ExcelData>  implements Serializable {

	/**
	 * 错误信息
	 */
	private List<List<ExcelError>> errors=new ArrayList<>();

	/**
	 * 数据集
	 */
	private List<T> data = new ArrayList<>();

	/**
	 * 是否存在有错误
	 * @return
	 */
	public Boolean isErrors(){
		return errors.size()>0?true:false;
	}

	public List<List<ExcelError>> getErrors() {
		return errors;
	}

	public void setErrors(List<List<ExcelError>> errors) {
		this.errors = errors;
	}

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}
}
