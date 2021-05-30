package com.starfire.familytree.folk.controller;


import com.starfire.familytree.enums.GenderEnum;
import com.starfire.familytree.exception.FamilyException;
import com.starfire.familytree.folk.entity.Member;
import com.starfire.familytree.folk.service.IChildrenService;
import com.starfire.familytree.folk.service.IMemberService;
import com.starfire.familytree.folk.service.IPartnerService;
import com.starfire.familytree.usercenter.entity.User;
import com.starfire.familytree.usercenter.service.IUserService;
import com.starfire.familytree.utils.ChineseNumber;
import com.starfire.familytree.utils.SessionHelper;
import com.starfire.familytree.utils.StringHelper;
import com.starfire.familytree.vo.*;
import io.swagger.annotations.Api;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
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
@Api(tags = "成员模块")
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
    public PageInfo<Map<String, Object>, Member> page(@RequestBody(required = false) PageInfo<Map<String, Object>, Member> page, HttpServletRequest request,@SessionAttribute("JSESSIONID") SecurityContextImpl securityContext) {
        Long userId = new SessionHelper().getUserId();
        page = page == null ? new PageInfo<>() : page;
        HashMap hashMap = new HashMap();
        hashMap.put("userId",userId);
        page.setParam(hashMap);
        PageInfo<Map<String, Object>, Member> pageInfo = memberService.page(page);
        return pageInfo;

    }



    @PostMapping("add")
    public Member addMember(@Valid Member member) {
        Member pl = memberService.addMember(member);
        return pl;
    }

    /**
     * 添加父母
     * @param parent
     * @param childId
     * @return
     */
    @PostMapping("addParent")
    public Member addParent(@Valid Member parent,Long childId,Boolean isFarther) {
        Member pl = memberService.addParent(parent,childId,isFarther);
        return pl;
    }

    /**
     * 添加配偶
     * @param wife
     * @param husbandId
     * @return
     */
    @PostMapping("addWife")
    public Member addWife(@Valid Member wife,Long husbandId) {
        Member pl = memberService.addWife(wife,husbandId);
        return pl;
    }

    /**
     * 添加孩子
     * @param child
     * @param parentId
     * @return
     */
    @PostMapping("addChildren")
    public Member addChildren(@Valid Member child,Long parentId) {
        Member pl = memberService.addChildren(child,parentId);
        return pl;
    }

    /**
     * 添加兄弟姐妹
     * @param siblings
     * @param brotherId
     * @return
     */
    @PostMapping("addSiblings")
    public Member addSiblings(@Valid Member siblings,Long brotherId) {
        Member pl = memberService.addSiblings(siblings,brotherId);
        return pl;
    }

    /**
     * 绑定上下级关系
     * @param param
     */
    @PostMapping("bindRelationship")
    public void bindRelationship(@RequestBody Map<String,String> param) {
       String memberId= param.get("memberId");
        String code=param.get("code");
        memberService.bindRelationship(Long.valueOf(memberId),code);
    }

    @GetMapping(name="get_members_by_generation")
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
        Long userId = new SessionHelper().getUserId();
        Member member = memberService.getMemberByUserId(userId);
        Long memberId = null;
        if (member!=null) {
            memberId=member.getId();
        }
        for (int i = 0; i < ids.length; i++) {
            Long id = Long.valueOf(ids[i]);
            if(id.equals(memberId)){
                throw new FamilyException("不能删除自己本人");
            }
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
            Long id = member.getId();
            member.setCode(Math.abs(id.hashCode())+"");
//            String pinyin = StringHelper.toPinyin(member.getFullName());
//            member.setPinyin(pinyin);
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
            Integer[] parents = new Integer[1];
            parents[0]=Math.abs(fatherId.hashCode());
            OrgChartItemVO orgChartItemVO = convertOrgChartItemVO(parents,husbandId,wife);
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
        Member husband = memberService.getFamilyTree(param);
        Integer generations = husband.getGenerations();
        Long fatherId = husband.getId();
        Long husbandId = husband.getId();
        //获取妻子
        Member wife = partnerService.getWife(husbandId);
        if(wife!=null){
            Integer[] parents = new Integer[1];
            parents[0]=Math.abs(fatherId.hashCode());
            OrgChartItemVO orgChartItemVO = convertOrgChartItemVO(parents,husbandId,wife);
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

    @PostMapping("/getMemberTree")
    public OrgChartVO getMemberTree(@RequestBody Map<String,String> param) {
        OrgChartVO orgChartVO = new OrgChartVO();
        String userId = param.get("userId");
        User user = userService.getById(userId);
        Member member = memberService.getMemberByUserId(user.getId());
		if(member!=null){
        Member husband = memberService.getForefatherByMemberId(member.getId());

			Integer generations = husband.getGenerations();
			Long fatherId = husband.getId();
			Long husbandId = husband.getId();
			//获取妻子
			Member wife = partnerService.getWife(husbandId);
			if(wife!=null){
				OrgChartItemVO orgChartItemVO = convertOrgChartItemVO(null,husbandId,wife);
				String avatar = wife.getAvatar();
				String brief = wife.getBrief();
				GenderEnum gender = wife.getGender();
				String sex = gender.name();
				orgChartItemVO.setSex(sex);
                orgChartItemVO.setCode(wife.getCode());
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
            orgChartItemVO.setCode(husband.getCode());
			orgChartItemVO.setParents(null);
			orgChartItemVO.setMemberId(fatherId+"");
			orgChartItemVO.setTitle(fullName);
			orgChartItemVO.setGenerations("第"+ ChineseNumber.numberToCH(generations)+"世");
			orgChartItemVO.setDescription(brief);
			orgChartItemVO.setRemark(husband.getRemark());
			orgChartVO.getItems().add(orgChartItemVO);
		}
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
            String code = children.getCode();
            GenderEnum gender = children.getGender();
            String sex =GenderEnum.不清楚.name();
            if(gender !=null){

                sex = gender.name();
            }
            Long childrenId = children.getId();
            //获取妻子
            Member wife = partnerService.getWife(childrenId);
            if(wife!=null){
                Integer[] parents = new Integer[1];
                parents[0]=Math.abs(fatherId.hashCode());
                OrgChartItemVO orgChartItemVO = convertOrgChartItemVO(parents,childrenId,wife);
                String wifeBrief = wife.getBrief();
                GenderEnum wifeGender = wife.getGender();
               Integer generations = wife.getGenerations();
                String wifesex = wifeGender.name();
                orgChartItemVO.setSex(wifesex);
                orgChartItemVO.setCode(wife.getCode());
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
            orgChartItemVO.setCode(code);
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


    private OrgChartItemVO convertOrgChartItemVO(Integer[]  parents,Long husbandId,Member wife) {
        String fullName = wife.getFullName();
        String brief = wife.getBrief();
        OrgChartItemVO orgChartItemVO = new OrgChartItemVO();
        orgChartItemVO.setId(Math.abs(wife.getId().hashCode()));
        orgChartItemVO.setParents(parents);
        orgChartItemVO.setMemberId(wife.getId()+"");
        orgChartItemVO.setTitle(fullName);
        orgChartItemVO.setDescription(brief);
        orgChartItemVO.setGenerations("第"+ ChineseNumber.numberToCH(wife.getGenerations())+"世");
        orgChartItemVO.setPosition(Math.abs(husbandId.hashCode()+1));
        orgChartItemVO.setRelativeItem(Math.abs(husbandId.hashCode()));


        return orgChartItemVO;
    }

    public static void main(String[] args) throws IOException {
        Collection<File> files = FileUtils.listFiles(new File("D:\\视频编辑工具\\图片"), new String[]{"svg"}, true);
        Iterator<File> iterator = files.iterator();
        while (iterator.hasNext()) {
            File next =  iterator.next();
            String name = next.getName();
            name=name.replace(" ","").replace("(","").replace(")","");
            FileUtils.copyFile(next,new File("D:\\视频编辑工具\\图片2\\"+name));
        }
    }

}
