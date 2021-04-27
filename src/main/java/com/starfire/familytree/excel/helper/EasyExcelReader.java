package com.starfire.familytree.excel.helper;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.ReadListener;
import com.starfire.familytree.exception.FamilyException;


import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 解析EasyExcel工具类
 */
public class EasyExcelReader {

    /**
     * 根据列头与数据范围 获取指定集合
     *
     * @param is
     * @param columns
     * @param importTypeE
     * @return
     */
    public static List<Map<String, Object>> readExcel(InputStream is, String[] columns, EasyExcelImportTypeE importTypeE) {
        if (is == null) {
            throw new FamilyException("导入Excel异常,无效输入流");
        }
        //创建监听器,赋予列头
        EasyExcelImportListener easyExcelImportListener = new EasyExcelImportListener(columns);
        //执行读方法
        EasyExcel.read(is).registerReadListener(easyExcelImportListener).sheet().doRead();
        if (importTypeE.getValue() == EasyExcelImportTypeE.数据.getValue()) {
            return easyExcelImportListener.getDatas();
        } else if (importTypeE.getValue() == EasyExcelImportTypeE.表头.getValue()) {
            return easyExcelImportListener.getHeads();
        } else if (importTypeE.getValue() == EasyExcelImportTypeE.表头与数据.getValue()) {
            return easyExcelImportListener.mergeHeadAndData();
        }
        throw new FamilyException("导入Excel异常,未传入数据范围");
    }

    /**
     * 根据实体类型 获取集合
     *
     * @param is
     * @return
     */
    public static <T extends ExcelData> ExcelResult<T>  readExcel(InputStream is, Class<T> t) {
        if (is == null) {
            throw new FamilyException("导入Excel异常,无效输入流");
        }
        if(t == null){
            throw new FamilyException("导入Excel异常,未指定实体");
        }
        //创建监听器,赋予列头
        ValidatorListener<T> validatorListener = new ValidatorListener<>(t);
        //执行读方法
        EasyExcel.read(is).registerReadListener(validatorListener).sheet().doRead();
        return validatorListener.getResult();
    }

    /**
     * 读取Excel 文件
     * @param is excel 输入流
     * @param readListener 每读取一行回调一次的监听器
     * @return
     */
    public static void readExcel(InputStream is, ReadListener readListener) {
        if (is == null) {
            throw new FamilyException("导入Excel异常,无效输入流");
        }
        EasyExcel.read(is).registerReadListener(readListener).sheet().doRead();
    }

    /**
     * 默认传入数据范围 根据列头获取集合
     *
     * @param is
     * @param columns
     * @return
     */
    public static List<Map<String, Object>> readExcel(InputStream is, String[] columns) {
        return readExcel(is, columns, EasyExcelImportTypeE.表头与数据);
    }

    /**
     * 根据数据范围 按数字排序返回集合
     *
     * @param is
     * @param importTypeE
     * @return
     */
    public static List<Map<String, Object>> readExcel(InputStream is, EasyExcelImportTypeE importTypeE) {
        return readExcel(is, null, importTypeE);
    }

    /**
     * 默认传入数据范围 按数字排序返回集合
     *
     * @param is
     * @return
     */
    public static List<Map<String, Object>> readExcel(InputStream is) {
        return readExcel(is, null, EasyExcelImportTypeE.表头与数据);
    }

}


