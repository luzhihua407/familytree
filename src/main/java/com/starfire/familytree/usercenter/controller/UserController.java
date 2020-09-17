package com.starfire.familytree.usercenter.controller;

import com.starfire.familytree.enums.MenuTypeEnum;
import com.starfire.familytree.response.Response;
import com.starfire.familytree.sys.entity.Menu;
import com.starfire.familytree.sys.entity.MenuRight;
import com.starfire.familytree.sys.entity.Role;
import com.starfire.familytree.sys.entity.RoleMenu;
import com.starfire.familytree.sys.service.*;
import com.starfire.familytree.usercenter.entity.Permission;
import com.starfire.familytree.usercenter.entity.RoleVO;
import com.starfire.familytree.usercenter.entity.User;
import com.starfire.familytree.usercenter.service.IUserService;
import com.starfire.familytree.vo.*;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
@RequestMapping("/user")
@Api(tags = "用户接口")
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IMenuRightService menuRightService;

    @Autowired
    private IRoleMenuService roleMenuService;

    @Autowired
    private IMenuService menuService;

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
        if (byUserName != null && user.getId() == null) {
            throw new RuntimeException("该用户名已存在，请换一个用户名");
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
            Long aLong = userId.get(i);
            String roles = String.valueOf(aLong);
//            user.getRoles().add(roles);
        }
        Response<User> response = new Response<>();
        return response.success(user);

    }

    @GetMapping("/current")
    public Response<User> current(Long userId) {
        User user = userService.getById(userId);
        List<Long> roleIds = userRoleService.getRoleIdsByUserId(userId);
        for (Long roleId : roleIds) {
            Role role = roleService.getById(roleId);
            RoleVO roleVo = new RoleVO();
            roleVo.setId(role.getId()+"");
            roleVo.setName(role.getName());
            //超管
            if(role.getCode().equals("admin")){
                List<Menu> menusByAdmin = menuService.getMenusByAdmin();
                for (int i = 0; i < menusByAdmin.size(); i++) {
                    Menu menu =  menusByAdmin.get(i);
                    convertMenu(roleVo, menu);
                }
            }else{
                List<RoleMenu> roleMenus = roleMenuService.getListByRoleId(roleId);
                for (int i = 0; i < roleMenus.size(); i++) {
                    RoleMenu roleMenu =  roleMenus.get(i);
                    Long menuId = roleMenu.getMenuId();
                    Menu menu = menuService.getById(menuId);
                    convertMenu(roleVo, menu);
                }
            }
            user.setRole(roleVo);
        }
        Response<User> response = new Response<>();
        return response.success(user);

    }

    private void convertMenu(RoleVO roleVo, Menu menu) {
        Permission pm = new Permission();
        pm.setPermissionId(menu.getCode());
        pm.setPermissionName(menu.getName());
        List<MenuRight> list = menuRightService.getList(menu.getId());
        for (int j = 0; j < list.size(); j++) {
            MenuRight menuRight = list.get(j);
            pm.getActionList().add(menuRight.getCode());
        }

        roleVo.getPermissions().add(pm);
    }

    /**
     * 分页
     *
     * @param page
     * @return
     * @author luzh
     */
    @PostMapping("/page")
    public Response<PageInfo<Map<String, Object>, User>> page(@RequestBody(required = false) PageInfo<Map<String, Object>, User> page) {
        page = page == null ? new PageInfo<>() : page;
        PageInfo<Map<String, Object>, User> pageInfo = userService.page(page);
        Response<PageInfo<Map<String, Object>, User>> response = new Response<PageInfo<Map<String, Object>, User>>();
        return response.success(pageInfo);

    }

    @PostMapping("/resetPassword")
    public Response resetPassword(@RequestBody ResetPasswordVO resetPasswordVO) {
        String password = resetPasswordVO.getPassword();
        String againPassword = resetPasswordVO.getAgainPassword();
        if (!password.equals(againPassword)) {
            throw new RuntimeException("两次密码不一致，请检查");
        }
        userService.resetPassword(resetPasswordVO);
        Response response = new Response();
        return response.success();

    }


}
