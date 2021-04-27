package com.starfire.familytree.excel.helper;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * 时间戳转换器
 */
public class TimestampConverter implements Converter<Timestamp> {


    @Override
    public Class<Timestamp> supportJavaTypeKey() {
        return Timestamp.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    @Override
    public Timestamp convertToJavaData(CellData cellData, ExcelContentProperty contentProperty,
									   GlobalConfiguration globalConfiguration) {
        return Timestamp.valueOf(cellData.getStringValue());
    }

    @Override
    public CellData<String> convertToExcelData(Timestamp value, ExcelContentProperty contentProperty,
											   GlobalConfiguration globalConfiguration) {
        return new CellData(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value));
    }

}
