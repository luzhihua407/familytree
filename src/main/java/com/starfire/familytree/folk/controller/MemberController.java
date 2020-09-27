package com.starfire.familytree.folk.controller;


import com.starfire.familytree.enums.GenderEnum;
import com.starfire.familytree.folk.entity.Member;
import com.starfire.familytree.folk.service.IChildrenService;
import com.starfire.familytree.folk.service.IPartnerService;
import com.starfire.familytree.folk.service.IMemberService;
import com.starfire.familytree.usercenter.entity.User;
import com.starfire.familytree.usercenter.service.IUserService;
import com.starfire.familytree.utils.ChineseNumber;
import com.starfire.familytree.utils.StringHelper;
import com.starfire.familytree.vo.*;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.text.NumberFormat;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author luzh
 * @since 2019-08-15
 */
@RestController
@RequestMapping("/folk/member")
@Api(tags = "人物模块")
public class MemberController {

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IPartnerService partnerService;

    @Autowired
    private IChildrenService childrenService;

    @Autowired
    private IUserService userService;

    /**
     * 分页
     *
     * @param page
     * @return
     * @author luzh
     */
    @PostMapping("/page")
    public PageInfo<Map<String, Object>, Member> page(@RequestBody(required = false) PageInfo<Map<String, Object>, Member> page) {
        page = page == null ? new PageInfo<>() : page;
        PageInfo<Map<String, Object>, Member> pageInfo = memberService.page(page);
        return pageInfo;

    }


    @PostMapping("add")
    public Member addMember(@RequestBody @Valid Member member) {
        Member pl = memberService.addMember(member);
        return pl;
    }

    @GetMapping("get_members_by_generation")
    public List<Member> getMembersByGeneration(@RequestParam(required = true) int gen) {
        List<Member> members = memberService.getMembersByGeneration(gen);
        return members;
    }

    @GetMapping("getMemberByName")
    public List<Member> getMemberByName(@RequestParam(required = true) String name) {
        List<Member> members = memberService.getMemberByName(name);
        return members;
    }

    @GetMapping("/get")
    public Member getMember(Long id) {
        Member member = memberService.getById(id);
        return member;
    }

    @GetMapping("/view")
    public Member viewMember(Long id) {
        Member member = memberService.getMember(id);
        return member;
    }

    @PostMapping("/delete")
    public Boolean deleteMember(@RequestBody DeleteVO<Long[]> deleteVO) {
        Long[] ids = deleteVO.getIds();
        for (int i = 0; i < ids.length; i++) {
            Long id = Long.valueOf(ids[i]);
            boolean b = memberService.removeById(id);

        }
        return true;
    }

    @PostMapping("/edit")
    public Boolean editMember(@RequestBody Member member) {
        String pinyin = StringHelper.toPinyin(member.getFullName());
        member.setPinyin(pinyin);
        boolean b = memberService.updateById(member);
        return b;
    }

    @PostMapping("/addRelationship")
    public Boolean addRelationship(@RequestBody RelationshipVO relationshipVO) {
        boolean b = memberService.addRelationship(relationshipVO);
        return b;
    }

    @PostMapping("/getNames")
    public List<Map<String,Object>> getNames(@RequestBody Map<String,String> param) {
        List<Map<String,Object>> names=new ArrayList<>();
        String name = param.get("name");
        if(name.length()>1) {

        names = memberService.getNames(name);
        }
        return names;
    }
    @GetMapping("/test")
    public void test() {
        List<Member> list = memberService.list();
        for (int i = 0; i < list.size(); i++) {
            Member member =  list.get(i);
            String pinyin = StringHelper.toPinyin(member.getFullName());
            member.setPinyin(pinyin);
            memberService.saveOrUpdate(member);
        }
    }

    @PostMapping("/tree")
    public OrgChartVO tree(@RequestBody Map<String,Integer> param) {
        OrgChartVO orgChartVO = new OrgChartVO();
        Member husband = memberService.getForefather(param.get("gen"));
        Long fatherId = husband.getId();
        Long husbandId = husband.getId();
        //获取妻子
        Member wife = partnerService.getWife(husbandId);
        if(wife!=null){
            OrgChartItemVO orgChartItemVO = convertOrgChartItemVO(husbandId,wife);
            orgChartVO.getItems().add(orgChartItemVO);
        }
        loopChildren(orgChartVO, husband,wife);
        String fullName = husband.getFullName();
        String brief = husband.getBrief();
        String avatar = husband.getAvatar();
        OrgChartItemVO orgChartItemVO = new OrgChartItemVO();
        orgChartItemVO.setId(Math.abs(fatherId.hashCode()));
        orgChartItemVO.setMemberId(fatherId+"");
        orgChartItemVO.setParents(null);
        orgChartItemVO.setTitle(fullName);
        orgChartItemVO.setDescription(brief);
        orgChartItemVO.setImage(avatar);
        orgChartVO.getItems().add(orgChartItemVO);
        return orgChartVO;
    }

    @PostMapping("/getFamilyTree")
    public OrgChartVO getFamilyTree(@RequestBody Map<String,String> param) {
        OrgChartVO orgChartVO = new OrgChartVO();
        String userId = param.get("userId");
        User user = userService.getById(userId);
        Member husband = memberService.getMember(user.getRealName());
//        Member husband = memberService.getFamilyTree(param);
        Integer generations = husband.getGenerations();
        Long fatherId = husband.getId();
        Long husbandId = husband.getId();
        //获取妻子
        Member wife = partnerService.getWife(husbandId);
        if(wife!=null){
            OrgChartItemVO orgChartItemVO = convertOrgChartItemVO(husbandId,wife);
            String avatar = wife.getAvatar();
            String brief = wife.getBrief();
            GenderEnum gender = wife.getGender();
            String sex = gender.name();
            orgChartItemVO.setSex(sex);
            orgChartItemVO.setMemberId(wife.getId()+"");
            orgChartItemVO.setGenerations("第"+ ChineseNumber.numberToCH(generations)+"世");
            orgChartItemVO.setDescription(brief);
            orgChartItemVO.setRemark(wife.getRemark());
            orgChartVO.getItems().add(orgChartItemVO);
        }
        loopChildren(orgChartVO, husband,wife);
        String fullName = husband.getFullName();
        String brief = husband.getBrief();
        GenderEnum gender = husband.getGender();
        OrgChartItemVO orgChartItemVO = new OrgChartItemVO();
        if(gender!=null){

        String sex = gender.name();
        orgChartItemVO.setSex(sex);
        }
        orgChartItemVO.setId(Math.abs(fatherId.hashCode()));
        orgChartItemVO.setParents(null);
        orgChartItemVO.setMemberId(fatherId+"");
        orgChartItemVO.setTitle(fullName);
        orgChartItemVO.setGenerations("第"+ ChineseNumber.numberToCH(generations)+"世");
        orgChartItemVO.setDescription(brief);
        orgChartItemVO.setRemark(husband.getRemark());
        orgChartVO.getItems().add(orgChartItemVO);
        return orgChartVO;
    }

    /**
     * 轮询取出所有后代
     * @param orgChartVO
     */
    private void loopChildren(OrgChartVO orgChartVO, Member father,Member mother) {
        Long fatherId=father.getId();
        Long motherId=null;
        if(mother!=null){
            motherId=mother.getId();
        }
        List<Member> childrenList = childrenService.getChildrenList(fatherId);
        for (int j = 0; j < childrenList.size(); j++) {
            Member children = childrenList.get(j);
            String fullName = children.getFullName();
            String brief = children.getBrief();
            GenderEnum gender = children.getGender();

            String sex = gender.name();
            Long childrenId = children.getId();
            //获取妻子
            Member wife = partnerService.getWife(childrenId);
            if(wife!=null){
                OrgChartItemVO orgChartItemVO = convertOrgChartItemVO(childrenId,wife);
                String wifeBrief = wife.getBrief();
                GenderEnum wifeGender = wife.getGender();
               Integer generations = wife.getGenerations();
                String wifesex = wifeGender.name();
                orgChartItemVO.setSex(wifesex);
                orgChartItemVO.setMemberId(wife.getId()+"");
                orgChartItemVO.setGenerations("第"+ ChineseNumber.numberToCH(generations)+"世");
                orgChartItemVO.setDescription(wifeBrief);
                orgChartItemVO.setRemark(wife.getRemark());
                orgChartVO.getItems().add(orgChartItemVO);
            }
            //获取孩子
            OrgChartItemVO orgChartItemVO = new OrgChartItemVO();
            orgChartItemVO.setId(Math.abs(childrenId.hashCode()));
            orgChartItemVO.setMemberId(childrenId+"");
            Integer[] parents=new Integer[2];
            parents[0]=Math.abs(fatherId.hashCode());
            if(motherId!=null){

            parents[1]=Math.abs(motherId.hashCode());
            }
            Integer generations = children.getGenerations();
            orgChartItemVO.setMemberId(children.getId()+"");
            orgChartItemVO.setParents(parents);
            orgChartItemVO.setTitle(fullName);
            orgChartItemVO.setLabel(children.getHeir());
            orgChartItemVO.setSex(sex);
            orgChartItemVO.setGenerations("第"+ ChineseNumber.numberToCH(generations)+"世");
            orgChartItemVO.setDescription(brief);
            orgChartItemVO.setRemark(children.getRemark());
            orgChartVO.getItems().add(orgChartItemVO);
            loopChildren(orgChartVO,children,wife);
        }
    }


    private OrgChartItemVO convertOrgChartItemVO(Long husbandId,Member wife) {
        String fullName = wife.getFullName();
        String brief = wife.getBrief();
        OrgChartItemVO orgChartItemVO = new OrgChartItemVO();
        orgChartItemVO.setId(Math.abs(wife.getId().hashCode()));
        orgChartItemVO.setParents(null);
        orgChartItemVO.setMemberId(wife.getId()+"");
        orgChartItemVO.setTitle(fullName);
        orgChartItemVO.setDescription(brief);
        orgChartItemVO.setGenerations("第"+ ChineseNumber.numberToCH(wife.getGenerations())+"世");
        orgChartItemVO.setPosition(Math.abs(husbandId.hashCode()+1));
        orgChartItemVO.setRelativeItem(Math.abs(husbandId.hashCode()));


        return orgChartItemVO;
    }

}
