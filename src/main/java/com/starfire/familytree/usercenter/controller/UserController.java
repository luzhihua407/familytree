package com.starfire.familytree.usercenter.controller;

import com.starfire.familytree.enums.MenuTypeEnum;
import com.starfire.familytree.response.Response;
import com.starfire.familytree.sys.entity.Menu;
import com.starfire.familytree.sys.entity.Role;
import com.starfire.familytree.sys.service.*;
import com.starfire.familytree.usercenter.entity.User;
import com.starfire.familytree.usercenter.service.IUserService;
import com.starfire.familytree.vo.*;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

/**
 * <p>
 * 用户控制器
 * </p>
 *
 * @author luzh
 * @since 2019-03-03
 */
@RestController
@RequestMapping("/usercenter/user")
@Api(tags = "用户接口")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private IMenuService menuService;


    @Autowired
    private IRoleService roleService;

    @Autowired
    private IRoleMenuRightService roleMenuRightService;

    @Autowired
    private IUserMenuRightService userMenuRightService;

    @Autowired
    private IUserRoleService userRoleService;

    /**
     * 新增或修改
     *
     * @param user
     * @return
     * @author luzh
     */
    @PostMapping("/addOrUpdate")
    public Response<User> addOrUpdateUser(@RequestBody(required = false) @Valid User user) {
        String username = user.getUsername();
        User byUserName = userService.getUserByUserName(username);
        if(byUserName!=null && user.getId()==null){
            throw  new  RuntimeException("该用户名已存在，请换一个用户名");
        }
        userService.saveOrUpdateUser(user);
        Response<User> response = new Response<User>();
        return response.success(user);

    }

    /**
     * 删除
     *
     * @return
     * @author luzh
     */
    @PostMapping("/delete")
    public Response<String> deleteUser(@RequestBody DeleteVO<Long[]> deleteVO) {
        boolean flag = false;
        Long[] ids = deleteVO.getIds();
        flag = userService.removeByIds(Arrays.asList(ids));
        Response<String> response = new Response<String>();
        if (!flag) {
            return response.failure();
        }
        return response.success();

    }
    /**
     * 获取单个用户
     *
     * @return
     * @author luzh
     */
    @GetMapping("/get")
    public Response<User> getUser(Long id) {
        User user = userService.getById(id);
        List<Long> userId = userRoleService.getRoleIdsByUserId(user.getId());
        for (int i = 0; i < userId.size(); i++) {
            Long aLong =  userId.get(i);
            String roles=String.valueOf(aLong);
            user.getRoles().add(roles);
        }
        Response<User> response = new Response<>();
        return response.success(user);

    }

    /**
     * 分页
     *
     * @param page
     * @return
     * @author luzh
     */
    @PostMapping("/page")
    public Response<PageInfo<Map<String, Object>, User>> page(@RequestBody(required = false)  PageInfo<Map<String, Object>, User> page) {
        page=page==null?new PageInfo<>():page;
        PageInfo<Map<String, Object>, User> pageInfo = userService.page(page);
        Response<PageInfo<Map<String, Object>, User>> response = new Response<PageInfo<Map<String, Object>, User>>();
        return response.success(pageInfo);

    }

    @PostMapping("/resetPassword")
    public Response resetPassword(@RequestBody ResetPasswordVO resetPasswordVO) {
       String password = resetPasswordVO.getPassword();
         String againPassword = resetPasswordVO.getAgainPassword();
         if(!password.equals(againPassword)){
             throw new RuntimeException("两次密码不一致，请检查");
         }
        userService.resetPassword(resetPasswordVO);
        Response response = new Response();
        return response.success();

    }


}
