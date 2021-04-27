package com.starfire.familytree.excel.helper;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import com.starfire.familytree.exception.FamilyException;
import com.starfire.familytree.utils.DateUtils;
import lombok.Data;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Data
public class ValidatorListener<T extends ExcelData> extends AnalysisEventListener<Map<String, String>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidatorListener.class);

    private ExcelResult<T> result = new ExcelResult<T>();

    /** 泛型的类型 */
    private Class<T> entityClass;

    public ValidatorListener(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    // 默认记录的行数
    public Integer row = 1;

    // 标明Excel数据是否已出错
    public Boolean flag = false;

    @Override
    public void invoke(Map<String, String> stringStringMap, AnalysisContext analysisContext) {
        Field[] declaredFields = entityClass.getDeclaredFields();
        List<ExcelError> excelErrors = new ArrayList<>();
        T t = null;
        try{
            t = entityClass.newInstance();
        } catch (Exception e){
            throw new FamilyException(e.getMessage());
        }
        for (Field declaredField : declaredFields) {
            if(declaredField.isAnnotationPresent(ExcelValidator.class)){
                ExcelValidator annotation = declaredField.getAnnotation(ExcelValidator.class);
                int index = annotation.index();
                boolean b = annotation.notEmpty();
                String regexp = annotation.regexp();
                String value = stringStringMap.get(index);
                // 先校验Null情况
                if(b && Strings.isEmpty(value)){
                    excelErrors.add(new ExcelError(row,declaredField.getName(),annotation.error()));
                    flag = true;
                    continue;
                }
                // 再验证格式是否满足
                if (Strings.isNotEmpty(value) && Strings.isNotEmpty(regexp) && !Pattern.matches(regexp, value)){
                    excelErrors.add(new ExcelError(row,declaredField.getName(),annotation.error()));
                    flag = true;
                    continue;
                }

                // 判断数据是否已报错,未报错则填充数据到实体中
                if(!flag){
                    String methodName = "set" + declaredField.getName().substring(0, 1).toUpperCase() + declaredField.getName().substring(1);
                    try{
                        Method method = entityClass.getDeclaredMethod(methodName, declaredField.getType());
                        Class o = Class.forName(declaredField.getType().getName());
                        if (o == String.class){
                            method.invoke(t,value);
                        } else if(o == Integer.class){
                            method.invoke(t, Integer.valueOf(value));
                        } else if(o == Double.class){
                            method.invoke(t, Double.valueOf(value));
                        } else if (o == Long.class){
                            method.invoke(t, Long.valueOf(value));
                        } else if (o == BigDecimal.class){
                            method.invoke(t, new BigDecimal(value));
                        } else if (o == Date.class){
                            method.invoke(t, DateUtils.parseDate(value));
                        } else if (o == Timestamp.class){
                            method.invoke(t,new Timestamp(DateUtils.parseDate(value).getTime()));
                        } else if (o == Float.class ){
                            method.invoke(t,Float.valueOf(value));
                        } else {
                            method.invoke(t,value);
                        }
                    } catch (Exception e){
                        throw new FamilyException(e.getMessage());
                    }
                }
            }
        }
        // 已报错则不填充数据
        if(flag){
            result.getErrors().add(excelErrors);
            result.getData().clear();
        } else {
            result.getData().add(t);
        }
        row++;
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        if(result.isErrors()){
            LOGGER.info("数据解析完成,数据有误!");
        } else {
            LOGGER.info("数据解析完成,无数据异常!");
        }
    }

    @Override
    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
        super.invokeHead(headMap, context);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        super.invokeHeadMap(headMap, context);
    }
}
