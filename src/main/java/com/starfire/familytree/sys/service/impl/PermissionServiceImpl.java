package com.starfire.familytree.sys.service.impl;

import com.starfire.familytree.sys.entity.Menu;
import com.starfire.familytree.sys.entity.MenuRight;
import com.starfire.familytree.sys.entity.Role;
import com.starfire.familytree.sys.entity.RoleMenu;
import com.starfire.familytree.sys.service.*;
import com.starfire.familytree.usercenter.entity.Permission;
import com.starfire.familytree.usercenter.entity.RoleVO;
import com.starfire.familytree.usercenter.entity.User;
import com.starfire.familytree.usercenter.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements IPermissionService {

    @Autowired
    private IUserRoleService userRoleService;

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IRoleMenuService roleMenuService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IMenuRightService menuRightService;

    @Override
    public RoleVO authorization(Long userId) {
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
            return roleVo;
        }
        return null;
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
}
