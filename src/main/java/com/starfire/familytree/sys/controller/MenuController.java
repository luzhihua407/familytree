package com.starfire.familytree.sys.controller;

import java.security.Principal;
import java.util.*;

import com.starfire.familytree.enums.MenuTypeEnum;
import com.starfire.familytree.sys.entity.MenuRight;
import com.starfire.familytree.sys.entity.Role;
import com.starfire.familytree.sys.service.IMenuRightService;
import com.starfire.familytree.sys.service.IUserRoleService;
import com.starfire.familytree.usercenter.entity.User;
import com.starfire.familytree.vo.*;
import io.swagger.annotations.Api;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import com.starfire.familytree.response.Response;
import com.starfire.familytree.sys.entity.Menu;
import com.starfire.familytree.sys.service.IMenuService;

import javax.validation.Valid;

/**
 * <p>
 * 菜单控制器
 * </p>
 *
 * @author luzh
 * @since 2019-03-03
 */
@RestController
@RequestMapping("/menu")
@Api(tags = "菜单接口")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @Autowired
    private IMenuRightService menuRightService;

    @Autowired
    private IUserRoleService userRoleService;
    /**
     * 新增或修改
     *
     * @param menu
     * @return
     * @author luzh
     */
    @PostMapping("/addOrUpdate")
    public Response<Menu> addOrUpdateMenu(@RequestBody @Valid Menu menu) {
        String code = menu.getCode();
        Menu menuByCode = menuService.getMenuByCode(code);
        if(menuByCode!=null && menu.getId()==null){
            throw new  RuntimeException("已存在该编码，请换一个编码");
        }
        menuService.saveOrUpdate(menu);
        List<MenuRightVO> menuRights = menu.getMenuRights();
        List<MenuRight> menuRightList=new ArrayList<>();
        for (int i = 0; i < menuRights.size(); i++) {
            MenuRightVO menuRightVO =  menuRights.get(i);
            MenuRight mr=new MenuRight();
            mr.setCode(menuRightVO.getKey());
            mr.setName(menuRightVO.getLabel());
            mr.setMenuId(menu.getId());
            menuRightList.add(mr);
        }
        menuRightService.removeByMenuId(menu.getId());
        menuRightService.saveBatch(menuRightList);
        Response<Menu> response = new Response<Menu>();
        return response.success(menu);

    }

    /**
     * 删除
     *
     * @return
     * @author luzh
     */
    @PostMapping("/delete")
    public Response<String> deleteMenu(@RequestBody DeleteVO<String[]> deleteVO) {
        String[] ids = deleteVO.getIds();
        boolean flag = menuService.removeByIds(Arrays.asList(ids));
        Response<String> response = new Response<String>();
        if (!flag) {
            return response.failure();
        }
        return response.success();

    }
    /**
     * 获取单个实体内容
     * @param id
     * @return
     */
    @GetMapping("/get")
    public Response<Menu> getUser(Long id) {
        Menu menu = menuService.getById(id);
        List<MenuRight> menuRights = menuRightService.getList(menu.getId());
        List<MenuRightVO> menuRightVOList = menuRightService.convert(menuRights);
        menu.setMenuRights(menuRightVOList);
        Response<Menu> response = new Response<Menu>();
        return response.success(menu);
    }
    /**
     * 获取所有父级菜单
     * @return
     */
    @GetMapping("/getParentMenus")
    public Response<List<Menu>> getParentMenus() {
        List<Menu> menus = menuService.getParentMenus();
        Response<List<Menu>> response = new Response<List<Menu>>();
        return response.success(menus);
    }
    /**
     * 获取菜单Tree
     * @return
     */
    @GetMapping("/getMenuTree")
    public Response<List<MenuTree>> getMenus() {
        List<MenuTree> menus = menuService.getMenusTree();
        Response<List<MenuTree>> response = new Response<List<MenuTree>>();
        return response.success(menus);
    }

    /**
     * 分页
     *
     * @param page
     * @return
     * @author luzh
     */
    @PostMapping("/page")
    public Response<PageInfo<Map<String, Object>, Menu>> page(@RequestBody(required = false) PageInfo<Map<String, Object>, Menu> page) {
        if(page==null){
            page=new PageInfo<>();
        }
        PageInfo<Map<String, Object>, Menu> pageInfo = menuService.page(page);
        Response<PageInfo<Map<String, Object>, Menu>> response = new Response<PageInfo<Map<String, Object>, Menu>>();
        return response.success(pageInfo);

    }

    @RequestMapping("/getMenuNav")
    public List<NavMenuTree> getMenuNav(@RequestBody Map<String,Long[]> roles) {
                List<NavMenuTree> list=new ArrayList<>();
                Long roleId = roles.get("roles")[0];
                Long userId = userRoleService.getUserIdsByRoleId(roleId);
                //分配给角色的菜单集合
                List<Menu> menus = menuService.getMenusByRoleId(roleId);
                //分配给用户的菜单集合
                List<Menu> menuList = menuService.getMenusByUserId(userId);
                //去重
                for (int i = 0; i < menuList.size(); i++) {
                    Menu menu =  menuList.get(i);
                    if(!menus.contains(menu)){
                        menus.add(menu);
                    }
                }

                for (Menu menu : menus) {
                    if(menu==null){
                        break;
                    }
                    MenuTypeEnum type = menu.getType();
                    NavMenuTree menuTree = new NavMenuTree();
                    menuTree.setName(menu.getCode());
                    menuTree.setId(menu.getId()+"");
                    menuTree.setComponent(menu.getUrl());
                    if(type==MenuTypeEnum.不可见菜单){
                        menuTree.getMeta().setShow(false);
                    }else{
                        menuTree.setParentId(menu.getParent()==null?"0":menu.getParent()+"");
                        menuTree.getMeta().setShow(true);
                    }
                    if(menu.getParent()==null){
                        menuTree.setComponent("RouteView");
                    }
                    menuTree.getMeta().setTitle(menu.getName());
                    menuTree.getMeta().setIcon(menu.getIcon());
                    menuTree.getMeta().setShow(true);
                    menuTree.setRedirect(menu.getUrl());
                    list.add(menuTree);
                }

        return list;
    }

}
