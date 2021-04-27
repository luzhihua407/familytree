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


    /**
     * 添加配偶
     * @param wife 妻子
     * @param husbandId 丈夫Id
     * @return
     */
    public Member addWife(Member wife,Long husbandId);

    /**
     * 添加父母
     * @param parent 父母
     * @param childId 小孩Id
     * @return
     */
    public Member addParent(Member parent,Long childId);

    /**
     * 添加兄弟姐妹
     * @param siblings 兄弟姐妹
     * @param brotherId 兄弟Id
     * @return
     */
    public Member addSiblings(Member siblings,Long brotherId);

    /**
     * 添加小孩
     * @param child 小孩
     * @param parentId 父母Id
     * @return
     */
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

    /**
     * 获取当前节点的祖先
     * @param currentMemberId
     * @return
     */
    public Member getForefatherByMemberId(Long currentMemberId);

    public Member getMember(Long id);

    public Member getFamilyTree(Map<String,String> param);

    public PageInfo<Map<String, Object>, Member> page(PageInfo<Map<String, Object>, Member> page);

    List<Member> getMemberByName(String name);

    public Member getMember(String name);

    boolean addRelationship(RelationshipVO relationshipVO);

    public List<Map<String,Object>> getNames(String name);

    /**
     * 绑定关系
     * @param memberId 自己的id
     * @param parentCode 上级编码
     */
    public void bindRelationship(Long memberId, String parentCode);
}
