package com.starfire.familytree.filter;

import com.starfire.familytree.response.Response;
import com.starfire.familytree.sys.entity.Menu;
import com.starfire.familytree.sys.entity.MenuRight;
import com.starfire.familytree.sys.entity.Role;
import com.starfire.familytree.sys.entity.RoleMenu;
import com.starfire.familytree.sys.service.*;
import com.starfire.familytree.usercenter.entity.Permission;
import com.starfire.familytree.usercenter.entity.RoleVO;
import com.starfire.familytree.usercenter.entity.User;
import com.starfire.familytree.utils.JacksonUtils;
import com.starfire.familytree.vo.RoleMenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 认证成功处理
 * @author luzh
 * @version 1.0.0
 * @ClassName MyAuthenticationSuccessHandler.java
 * @Description TODO
 * @createTime 2020年09月01日 11:24:00
 */
@Component
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

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

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        if(principal instanceof User){
            User user=(User) principal;
            Long id = user.getId();
            List<Long> roleIds = userRoleService.getRoleIdsByUserId(id);
            for (Long roleId : roleIds) {
                Role role = roleService.getById(roleId);
                GrantedAuthority ga = new SimpleGrantedAuthority("ROLE_"+role.getCode());
                user.getAuthorities().add(ga);
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
                user.getRoles().add(roleVo);
            }
        }
        Response<Object> response = new Response<Object>();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        response.setResult(principal);
        String json = JacksonUtils.toString(response);
        httpServletResponse.getWriter().print(json);
    }

    private void convertMenu(RoleVO roleVo, Menu menu) {
        Permission pm = new Permission();
        pm.setPermissionId(menu.getCode());
        pm.setPermissionName(menu.getName());
        List<MenuRight> list = menuRightService.getList(menu.getId());
        for (int j = 0; j < list.size(); j++) {
            MenuRight menuRight =  list.get(j);
            pm.getActionList().add(menuRight.getCode());
        }

        roleVo.getPermissions().add(pm);
    }
}
