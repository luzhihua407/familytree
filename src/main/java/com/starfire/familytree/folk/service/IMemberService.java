package com.starfire.familytree.folk.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.starfire.familytree.folk.entity.CategoryContent;
import com.starfire.familytree.folk.entity.Member;
import com.starfire.familytree.vo.PageInfo;
import com.starfire.familytree.vo.RelationshipVO;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author luzh
 * @since 2019-08-09
 */
public interface IMemberService extends IService<Member> {


    public Member addWife(Member wife,Long husbandId);

    public Member addChildren(Member child,Long parentId);

    public Member getHusband(Long husbandId);

    public Member addMember(Member member) ;


    public List<Member> getMembersByGeneration(int gen);

    /**
     * 获取祖先第一人
     * @param gen
     * @return
     */
    public Member getForefather(int gen);

    public Member getMember(Long id);

    public Member getFamilyTree(Map<String,String> param);

    public PageInfo<Map<String, Object>, Member> page(PageInfo<Map<String, Object>, Member> page);

    List<Member> getMemberByName(String name);

    public Member getMember(String name);

    boolean addRelationship(RelationshipVO relationshipVO);

    public List<Map<String,Object>> getNames(String name);
}
