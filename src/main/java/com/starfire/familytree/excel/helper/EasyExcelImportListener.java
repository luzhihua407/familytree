package com.starfire.familytree.excel.helper;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EasyExcelImportListener extends AnalysisEventListener<Map<Integer, String>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EasyExcelImportListener.class);

    /**
     * 创建监听器携带对应列头
     */
    public EasyExcelImportListener(String[] columns) {
        this.columns = columns;
    }

    public EasyExcelImportListener() {
    }

    /**
     * 存放列头
     */
    private String[] columns;

    /**
     * 数据集合
     */
    private List<Map<String, Object>> datas = new ArrayList<Map<String, Object>>();
    /**
     * 表头集合
     */
    private List<Map<String, Object>> heads = new ArrayList<Map<String, Object>>();

    /**
     * 填充数据
     *
     * @param data
     * @param context
     */
    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context) {
        if (!data.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            if (columns != null && columns.length > 0) {
                for (int i = 0; i < data.size(); i++) {
                    String val = data.get(i);
                    try {
                        map.put(columns[i], val == null ? "" : val);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            } else {
                for (int i = 0; i < data.size(); i++) {
                    String val = data.get(i);
                    map.put(i + "", val == null ? "" : val);
                }
            }
            datas.add(map);
        }
    }

    /**
     * 重写表头执行方法,将表头项也放入集合中
     *
     * @param headMap
     * @param context
     */
    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        if (!headMap.isEmpty()) {
            Map<String, Object> map = new HashMap<>();
            if (columns != null && columns.length > 0) {
                for (int i = 0; i < headMap.size(); i++) {
                    String val = headMap.get(i);
                    try {
                        map.put(columns[i], val);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage());
                    }
                }
            } else {
                for (int i = 0; i < headMap.size(); i++) {
                    String val = headMap.get(i);
                    map.put(i + "", val);
                }
            }
            heads.add(map);
        }
    }

    /**
     * 返回存放表头与数据集合
     *
     * @return
     */
    public List<Map<String, Object>> mergeHeadAndData() {
        List<Map<String, Object>> result = null;
        if (!datas.isEmpty() && !heads.isEmpty()) {
            result = new ArrayList<>();
            result.addAll(heads);
            result.addAll(datas);
        }
        return result;
    }

    /**
     * 数据填充完成后会调用此方法
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        LOGGER.info("数据解析完成！");
    }

    public List<Map<String, Object>> getDatas() {
        return datas;
    }

    public List<Map<String, Object>> getHeads() {
        return heads;
    }

    public String[] getColumns() {
        return columns;
    }

    public void setColumns(String[] columns) {
        this.columns = columns;
    }
}
