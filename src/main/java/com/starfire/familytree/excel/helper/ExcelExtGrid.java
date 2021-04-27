package com.starfire.familytree.excel.helper;


import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 映射到ExtjsGrid的 Excel导出格式
 */
public class ExcelExtGrid implements Serializable{

	/**
	 * 返回默认文件名
	 *
	 * @return
	 */
	static final String getDefaultExportFileName() {
		return "export.xlsx";
	}


	private String fileId;

	private String fileName;

	/**
	 * 增加 Grid column
	 *
	 * @param header
	 * @param width
	 * @param dataIndex
	 */
	public void addGridColumn(String header, Integer width, String dataIndex, String xtype) {
		GridColumn col = new GridColumn();
		col.header = header;
		col.width = width;
		col.dataIndex = dataIndex;
		col.xtype = xtype;
		this.columns.add(col);
	}

	/**
	 * 表列布局
	 */
	private List<GridColumn> columns = new ArrayList<GridColumn>();

	/**
	 * extjs grid的属性映射
	 */
	public static class GridColumn implements Serializable{
		/**
		 * 列头
		 */
		public String header;

		/**
		 * 列宽度
		 */
		public Integer width;

		/**
		 * 访问值
		 */
		public String dataIndex;

		/**
		 * 数据类型
		 */
		public String xtype;
	}

	public String getFileName() {
		if(StringUtils.isEmpty(fileName)){
			fileName=getDefaultExportFileName();
		}
		return fileName;
	}

	public List<GridColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<GridColumn> columns) {
		this.columns = columns;
	}
}


