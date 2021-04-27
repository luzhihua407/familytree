package com.starfire.familytree.excel.helper;

/**
 * 获取的数据范围
 */
public enum EasyExcelImportTypeE {
    表头(0), 数据(1), 表头与数据(2);
    private int value;

    EasyExcelImportTypeE(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
