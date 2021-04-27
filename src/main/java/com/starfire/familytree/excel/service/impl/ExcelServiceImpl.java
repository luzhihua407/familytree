package com.starfire.familytree.excel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starfire.familytree.enums.GenderEnum;
import com.starfire.familytree.excel.helper.EasyExcelReader;
import com.starfire.familytree.excel.service.ExcelHeaderEnum;
import com.starfire.familytree.excel.service.IExcelService;
import com.starfire.familytree.folk.entity.Member;
import com.starfire.familytree.folk.mapper.MemberMapper;
import org.springframework.stereotype.Service;
import java.io.InputStream;
import java.util.*;

/**
 * @author luzh
 * @version 1.0.0
 * @ClassName ExcelServiceImpl.java
 * @Description TODO
 * @createTime 2019年12月12日 14:39:00
 */
@Service
public class ExcelServiceImpl extends ServiceImpl<MemberMapper, Member> implements IExcelService {

    @Override
    public void importMember(InputStream inp) {
        List<Map<String, Object>> list =  EasyExcelReader.readExcel(inp);
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> map =  list.get(i);
            Object name = map.get(ExcelHeaderEnum.姓名.name());
            Object gender = map.get(ExcelHeaderEnum.性别.name());
            Object generations =map.get(ExcelHeaderEnum.第几世.name());
            if(name!=null && gender!=null && generations!=null){
                Member member = new Member();
                member.setFullName((String) name);
                if("男".equals(gender)){
                    member.setGender(GenderEnum.男);
                }
                if("女".equals(gender)){
                    member.setGender(GenderEnum.女);
                }
                member.setGenerations(((Double)generations).intValue());
                this.save(member);
            }else{
                throw new RuntimeException("第"+(i+1)+"行，【姓名】，【性别】、【第几世】不能为空");
            }

        }

    }

}
