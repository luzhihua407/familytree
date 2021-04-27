package com.starfire.familytree.folk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starfire.familytree.folk.entity.Member;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author luzh
 * @since 2019-08-09
 */
public interface MemberMapper extends BaseMapper<Member> {

    public List<Member> getMembersByGeneration(@Param("gen") int gen);

    public Member getForefather(@Param("gen") int gen);

    Page<Member> getPage(Page<Member> page, @Param("param")Map<String, Object> param);

    Member getFamilyTree(String branch);

    List<Member> getMemberByName(@Param("name")String name);

    public String getBranchByName(@Param("name")String name);

    public List<Map<String,Object>> getNamesByPinyin(@Param("pinyin")String pinyin);

    public List<Map<String, Object>> getMemberNumByGender(@Param("villageCode")String villageCode);

    public List<Map<String, Object>> getMemberNumByEducation(@Param("villageCode")String villageCode);

    public List<Map<String, Object>> getMemberNumByProTeam(@Param("villageCode")String villageCode);

    public List<Map<String, Object>> getMemberNumByBranch(@Param("villageCode")String villageCode);

    public List<Map<String, Object>> getGenderByGenerations(@Param("villageCode")String villageCode);

    /**
     * 根据编码获取实体
     * @param code
     */
    public Member getMemberByCode(String code);
}
