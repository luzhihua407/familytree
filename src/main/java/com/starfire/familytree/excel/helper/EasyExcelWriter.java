package com.starfire.familytree.excel.helper;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 生成EasyExcel工具类
 */
@SuppressWarnings("unchecked")
public class EasyExcelWriter {

    /**
     * 返回默认文件名
     *
     * @return
     */
    static final String getDefaultExportFileName() {
        return "export.xlsx";
    }

    /**
     * 构造函数需要的配置
     */
    ExcelExtGrid grid = null;

    /**
     * 填充数据所需要列表数据
     */
    List data = null;


    /**
     * 通过ExtjsGrid的Column配置导出
     *
     * @param grid grid配置（列）
     * @param data 查询数据
     */
    public EasyExcelWriter(ExcelExtGrid grid, List data) {
        this.grid = grid;
        this.data = data;
    }

    public void write(OutputStream ouputStream) throws Exception {
        String fileName = grid.getFileName();
        fileName = fileName.endsWith(".xlsx") ? fileName : fileName + ".xlsx";
        //表头与数据映射的集合
        List<Map<String, Object>> maps = headMap();
        //数据集合
        List<List<Object>> lists = dataList(maps);
        //表头集合
        List<List<String>> heads = head();

        //获取头样式
        WriteCellStyle headStyle = getHeadStyle();
        //获取内容样式
        WriteCellStyle contentStyle = getContentStyle();
        //样式策略
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headStyle, contentStyle);
        //写出文件
        ExcelWriterBuilder write = EasyExcel.write(ouputStream);
        write.registerConverter(new TimestampConverter())
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .registerWriteHandler(horizontalCellStyleStrategy).autoCloseStream(Boolean.FALSE)
                .head(heads).sheet(0, "sheet").doWrite(lists);
    }

    //导出的Excel表头集合
    private List<List<String>> head() {
        List<ExcelExtGrid.GridColumn> columns = grid.getColumns();
        if (columns != null && columns.size() > 0) {
            List<List<String>> heads = new ArrayList<>();
            ArrayList<String> temp = null;
            for (ExcelExtGrid.GridColumn column : columns) {
                temp = new ArrayList<>();
                temp.add(column.header);
                heads.add(temp);
            }
            return heads;
        }
        return null;
    }

    //数据库与Excel表头对应集合
    private List<Map<String, Object>> headMap() {
        List<ExcelExtGrid.GridColumn> columns = grid.getColumns();
        if (columns != null && columns.size() > 0) {
            List<Map<String, Object>> heads = new ArrayList<>();
            Map<String, Object> temp = null;
            for (ExcelExtGrid.GridColumn column : columns) {
                temp = new HashMap<>();
                temp.put(column.dataIndex, column.header);
                heads.add(temp);
            }
            return heads;
        }
        return null;
    }

    //导出Excel内容集合,与按照表头的顺序插入
    private List<List<Object>> dataList(List<Map<String, Object>> head) {
        List<List<Object>> list = new ArrayList<List<Object>>();
        if(!head.isEmpty()){
            for (int i = 0; i < data.size(); i++) {
                Map o = (Map) data.get(i);
                List<Object> data = new ArrayList<Object>();
                for (int j = 0; j < head.size(); j++) {
                    Map<String, Object> map = head.get(j);
                    Object o1 = o.get(map.keySet().iterator().next());
                    if (o1 instanceof Integer) {
                        data.add(Integer.valueOf(String.valueOf(o1)));
                    } else if (o1 instanceof Long) {
                        data.add((Long) o1);
                    } else if (o1 instanceof BigDecimal) {
                        BigDecimal bigDecimal = (BigDecimal) o1;
                        data.add(String.format("%.2f",Double.parseDouble(bigDecimal.toString())));//BigDecimal类型保留两位小数
                    } else if (o1 instanceof Double) {
                        data.add(String.format("%.2f",(Double)o1));//Double类型保留两位小数
                    } else if (o1 == null){
                        data.add("");//为避免数据对应不上,当出现数据为空时 默认补上空字符串
                    } else {
                        data.add(o1);
                    }
                }
                list.add(data);
            }
        }
        return list;
    }

    /**
     * 获取列头样式
     *
     * @return WriteCellStyle
     */
    private WriteCellStyle getHeadStyle() {
        //设置头样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("宋体");
        headWriteFont.setFontHeightInPoints((short) 10);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);// 左右居中
        headWriteCellStyle.setWrapped(true);
        headWriteCellStyle.setLeftBorderColor((short)8);
        headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        headWriteCellStyle.setRightBorderColor((short)8);
        headWriteCellStyle.setBorderRight(BorderStyle.THIN);
        headWriteCellStyle.setBorderBottom(BorderStyle.THIN); // 设置单元格的边框为粗体
        headWriteCellStyle.setBottomBorderColor((short)8); // 设置单元格的边框颜色．
        headWriteCellStyle.setFillForegroundColor((short)9);// 设置单元格的背景颜色．
        //headWriteCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#.##"));
        return headWriteCellStyle;
    }

    /**
     * 获取内容样式
     *
     * @return WriteCellStyle
     */
    private WriteCellStyle getContentStyle() {
        // 内容的策略
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        // 这里需要指定 FillPatternType 为FillPatternType.SOLID_FOREGROUND 不然无法显示背景颜色.头默认了 FillPatternType所以可以不指定
        contentWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);// 左右居中
        contentWriteCellStyle.setWrapped(true);
        contentWriteCellStyle.setLeftBorderColor((short)8);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setRightBorderColor((short)8);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN); // 设置单元格的边框为粗体
        contentWriteCellStyle.setBottomBorderColor((short)8); // 设置单元格的边框颜色．
        contentWriteCellStyle.setFillForegroundColor((short)9);// 设置单元格的背景颜色．
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("宋体");
        // 字体大小
        contentWriteFont.setFontHeightInPoints((short) 9);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        return contentWriteCellStyle;
    }
}
