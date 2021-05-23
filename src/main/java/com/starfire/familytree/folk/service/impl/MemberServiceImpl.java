package com.starfire.familytree.folk.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starfire.familytree.basic.entity.Dict;
import com.starfire.familytree.basic.service.IDictService;
import com.starfire.familytree.enums.BooleanEnum;
import com.starfire.familytree.exception.FamilyException;
import com.starfire.familytree.folk.entity.Children;
import com.starfire.familytree.folk.entity.Partner;
import com.starfire.familytree.folk.entity.Member;
import com.starfire.familytree.folk.mapper.ChildrenMapper;
import com.starfire.familytree.folk.mapper.PartnerMapper;
import com.starfire.familytree.folk.mapper.MemberMapper;
import com.starfire.familytree.folk.service.IMemberService;
import com.starfire.familytree.utils.ChineseNumber;
import com.starfire.familytree.utils.StringHelper;
import com.starfire.familytree.vo.PageInfo;
import com.starfire.familytree.vo.RelationshipVO;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.Positive;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author luzh
 * @since 2019-08-09
 */
@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements IMemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private ChildrenMapper childrenMapper;

    @Autowired
    private PartnerMapper partnerMapper;

    @Autowired
    private IDictService dictService;

    @Override
    public PageInfo<Map<String, Object>, Member> page(PageInfo<Map<String, Object>, Member> pageInfo) {
        Map<String, Object> param = pageInfo.getParam();
        Page<Member> page = pageInfo.toMybatisPlusPage();
        Page<Member> result = memberMapper.getPage(page, param);
        pageInfo.from(result);
        return pageInfo;
    }

    @Override
    public List<Member> getMemberByName(String name) {
        return memberMapper.getMemberByName(name);
    }

    @Override
    public Member getMember(String name) {
        List<Member> memberList = memberMapper.getMemberByName(name);
        if(memberList!=null && memberList.size()>0){
            return memberList.get(0);
        }
        return null;
    }

    @Override
    public boolean addRelationship(RelationshipVO relationshipVO) {
        Long husbandId = relationshipVO.getHusbandId();
        Long wifeId = relationshipVO.getWifeId();
        Long[] childrenIds = relationshipVO.getChildrenIds();
        if(childrenIds!=null){

            for (int i = 0; i < childrenIds.length; i++) {
                Long childrenId = childrenIds[i];
                Children children = new Children();
                children.setChildrenId(childrenId);
                children.setParentId(husbandId);
                childrenMapper.insert(children);
            }
        }
        if(wifeId!=null){

            Partner partner=new Partner();
            partner.setHusbandId(husbandId);
            partner.setWifeId(wifeId);
            partnerMapper.insert(partner);
        }
        return true;
    }

    @Override
    public List<Map<String,Object>> getNames(String name) {
        List<Map<String, Object>> names=new ArrayList<>();
        if(StringUtils.isNotEmpty(name)){
            names = memberMapper.getNamesByPinyin(name);
            for (int i = 0; i < names.size(); i++) {
                Map<String, Object> map =  names.get(i);
                Long id = (Long)map.get("id");
                map.put("id",Math.abs(id.hashCode())+"");
            }
        }
        return names;
    }

    @Override
    @Transactional
    public void bindRelationship(Long memberId, String parentCode) {
        Member parentMember = memberMapper.getMemberByCode(parentCode);

        Member member = memberMapper.selectById(memberId);

        Children children = childrenMapper.getEntityByChildrenId(memberId);
        if(children!=null){
            if(parentMember!=null){

                children.setParentId(parentMember.getId());
                childrenMapper.updateById(children);
            }
        }else {
            if(parentMember!=null){
                addChildren(member,parentMember.getId());
            }
        }
    }

    /**
     * 保存妻子和相关关系，并设置为已婚,是否有孩子跟丈夫一样
     *
     * @param wife
     * @param husbandId
     * @return
     */
    @Override
    public Member addWife(Member wife, Long husbandId) {
        if(husbandId==null){
            throw new FamilyException(husbandId+"不能为空");
        }
        Member husband = memberMapper.selectById(husbandId);
        husband.setIsMarried(BooleanEnum.是);
        memberMapper.updateById(husband);
        wife.setIsMarried(BooleanEnum.是);
        wife.setHasChild(husband.getHasChild());
        memberMapper.insert(wife);
        Partner partner = new Partner();
        partner.setWifeId(wife.getId());
        partner.setHusbandId(husbandId);
        partnerMapper.insert(partner);
        return wife;
    }

    @Override
    public Member addParent(Member member,Long childId,Boolean isFarther) {
        if(childId==null){
            throw new FamilyException(childId+"不能为空");
        }
        memberMapper.insert(member);
        //如果已存在父亲或者母亲
        Member parent = childrenMapper.getParent(childId);
        if(parent!=null){
            //把父亲和母亲设置为配偶关系
            Partner partner = new Partner();
            if(isFarther){
                partner.setWifeId(parent.getId());
                partner.setHusbandId(member.getId());
            }else {
                partner.setWifeId(member.getId());
                partner.setHusbandId(parent.getId());
            }
            partnerMapper.insert(partner);

        }else{
            //设置父子关系
            Children children = new Children();
            children.setChildrenId(childId);
            children.setParentId(member.getId());
            childrenMapper.insert(children);
        }
        return member;
    }

    @Override
    public Member addSiblings(Member siblings, Long brotherId) {
        if(brotherId==null){
            throw new FamilyException(brotherId+"不能为空");
        }
        Member parent = childrenMapper.getParent(brotherId);
        if(parent==null){
            throw new FamilyException("请先添加父母");
        }
        memberMapper.insert(siblings);
        Children children = new Children();
        children.setChildrenId(siblings.getId());
        children.setParentId(parent.getId());
        childrenMapper.insert(children);
        return siblings;
    }

    /**
     * 保存孩子，设置父子关系，并标记父类有孩子
     *
     * @param child
     * @param parentId
     * @return
     */
    @Override
    @Transactional
    public Member addChildren(Member child, Long parentId) {
        if(parentId==null){
            throw new FamilyException(parentId+"不能为空");
        }
        Member parent = memberMapper.selectById(parentId);
        parent.setHasChild(BooleanEnum.是);
        Integer generations = parent.getGenerations();
        memberMapper.updateById(parent);
        //世代在父的基础上+1
        child.setGenerations(generations + 1);
        memberMapper.insert(child);
        Children children = new Children();
        children.setParentId(parentId);
        children.setChildrenId(child.getId());
        childrenMapper.insert(children);
        return child;

    }

    @Override
    public Member getHusband(Long husbandId) {
        Member husband = memberMapper.selectById(husbandId);
        return husband;
    }

    @Override
    public Member addMember(Member member)  {
        String pinyin = StringHelper.toPinyin(member.getFullName());
        member.setPinyin(pinyin);
        memberMapper.insert(member);
        return member;
    }

    @Override
    public List<Member> getMembersByGeneration(int gen) {
        List<Member> members = memberMapper.getMembersByGeneration(gen);
        return members;
    }

    @Override
    public Member getForefather(int gen) {
        return memberMapper.getForefather(gen);
    }

    @Override
    public Member getForefatherByMemberId(Long currentMemberId) {
        if(currentMemberId==null){
            return null;
        }
        //循环去取最顶级的祖先,如果还没有，则返回自己
        Member parent = childrenMapper.getParent(currentMemberId);
        Member result=null;
        if(parent==null){
            return memberMapper.selectById(currentMemberId);
        }
        while(true){
            if(parent == null){
                return  result;
            }else{
                result = parent;
            }
            Long childId = parent.getId();
            parent = childrenMapper.getParent(childId);
        }
    }

    @Override
    public Member getMember(Long id) {
        Dict dict=null;
        Member member = this.getById(id);
        Long memberBranch = member.getMemberBranch();
        if(memberBranch!=null){

            dict = dictService.getById(memberBranch);
            member.setBranchName(dict.getName());
        }
        Integer generations = member.getGenerations();
        member.setGenerationsText("第"+ ChineseNumber.numberToCH(generations)+"世");
        Long prodTeam = member.getProdTeam();
        if(prodTeam!=null){

        dict = dictService.getById(prodTeam);
        member.setProdTeamName(dict.getName());
        }
        String education = member.getEducation();
        if(StringUtils.isNotEmpty(education)){

        dict = dictService.getDict(education);
        member.setEducation(dict.getName());
        }
        Date death = member.getDeath();
        Date birth = member.getBirth();
        if(death!=null && birth!=null){
            long time = death.getTime() - birth.getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(time));
            long totalDay = time/(24*60*60*1000);// a day
            long aliveAge = totalDay / 365;
            member.setAliveAge((int)aliveAge);
        }
        return member;
    }

    @Override
    public Member getFamilyTree(Map<String,String> param) {
        String branch=param.get("branch");
        String name=param.get("name");

        if(StringUtils.isNotEmpty(name)){
            String branchId = memberMapper.getBranchByName(name);

            if(StringUtils.isEmpty(branchId)){
                throw  new RuntimeException("搜索不到"+name);
            }else{
                branch=branchId;
            }
        }
        return memberMapper.getFamilyTree(branch);
    }
}
