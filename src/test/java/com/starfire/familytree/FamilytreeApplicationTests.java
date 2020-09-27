package com.starfire.familytree;

import com.starfire.familytree.enums.ValidEnum;
import com.starfire.familytree.folk.entity.*;
import com.starfire.familytree.folk.service.*;
import com.starfire.familytree.sys.entity.Role;
import com.starfire.familytree.sys.entity.UserRole;
import com.starfire.familytree.sys.service.IMenuService;
import com.starfire.familytree.sys.service.IRoleService;
import com.starfire.familytree.sys.service.IUserRoleService;
import com.starfire.familytree.usercenter.entity.User;
import com.starfire.familytree.usercenter.service.IUserService;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FamilytreeApplicationTests {

    @Autowired
    private RestTemplate rt;

    @Autowired
    private IMemberService memberService;

    @Autowired
    private IChildrenService childrenService;

    @Autowired
    private IPartnerService partnerService;

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ICategoryContentService categoryContentService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IMenuService menuService;

    @Test
    public void text() throws IOException, URISyntaxException {



    }
    @Test
    public void addChildren() {
        Member member = new Member();
        member.setFullName("开");
        Member father = memberService.addMember(member);
        Member children = new Member();
        children.setFullName("华");
        Member child = memberService.addChildren(children,father.getId());
        Member third = new Member();
        third.setFullName("静");
        Member fourth = memberService.addChildren(third,children.getId());

    }

    @Test
    public void addWife() {
        Member husband = memberService.getHusband(1167415966955597826L);
        Member wife = new Member();
        wife.setFullName("容");
        Member wif = memberService.addWife(wife,husband.getId());

    }

    @Test
    public void contextLoads2() {
        List<Member> members = memberService.getMembersByGeneration(1);
        for (int i = 0; i < members.size(); i++) {
            Member member = members.get(i);
            System.err.println(member.getFullName());
        }

    }

    @Test
    public void category() {
        Category category = new Category();
        category.setName("捐款榜");
        categoryService.save(category);


    }

    @Test
    public void categoryContent() {
        CategoryContent categoryContent = new CategoryContent();
        categoryContent.setTitle("人物事迹-张三1");
        categoryContent.setContent("<font>张三是好人1</font>");
        categoryContent.setCategoryId(1161093219849654274l);
        categoryContentService.save(categoryContent);


    }

    @Test
    public void user() {
        User user = new User();
        user.setRealName("管理员");
        user.setUsername("admin");
        user.setValid(ValidEnum.是);
        user.setPassword("admin");
        user.setEmail("user@126.com");
        userService.registerNewUserAccount(user);


    }
    @Test
    public void role() {
        Role role=new Role();
        role.setCode("USER");
        role.setName("USER");
        roleService.save(role);


    }
    @Test
    public void userRole() {
        User user = userService.getUserByEmail("user@126.com");

        Role role = roleService.getRoleByCode("USER");
        UserRole userRole=new UserRole();
        userRole.setRoleId(role.getId());
        userRole.setUserId(user.getId());
        userRoleService.save(userRole);


    }

    @Test
    public void addUserRole() {
        Member p = memberService.getById("1167241893457801217");
        Member member=new Member();
        member.setFullName("静");
        memberService.save(member);
    }

}
