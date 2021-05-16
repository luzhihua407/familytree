package com.starfire.familytree.sys.controller;

import java.util.*;

import com.starfire.familytree.basic.entity.Dict;
import com.starfire.familytree.basic.service.IDictService;
import com.starfire.familytree.enums.MenuTypeEnum;
import com.starfire.familytree.sys.entity.MenuRight;
import com.starfire.familytree.sys.entity.Role;
import com.starfire.familytree.sys.service.IMenuRightService;
import com.starfire.familytree.sys.service.IRoleService;
import com.starfire.familytree.sys.service.IUserRoleService;
import com.starfire.familytree.vo.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IDictService dictService;
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
        List<String> menuRights = menu.getMenuRights();
        List<MenuRight> menuRightList=new ArrayList<>();
        for (int i = 0; i < menuRights.size(); i++) {
            String dictCode = menuRights.get(i);
            Dict dict = dictService.getDictByCode("opt_permission",dictCode);
            MenuRight mr=new MenuRight();
            mr.setCode(dict.getCode());
            mr.setName(dict.getName());
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
        List<String> menuRightVOList = menuRightService.convertStringList(menuRights);
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
    private List<NavMenuTree> convertMenu(List<Menu> menus) {
        List<NavMenuTree> toList=new ArrayList<>();
        for (Menu menu : menus) {
            String menuCode = menu.getCode();
            MenuTypeEnum type = menu.getType();
            NavMenuTree menuTree = new NavMenuTree();
            menuTree.setName(menuCode);
            menuTree.setId(menu.getId()+"");
            menuTree.setRedirect(menu.getRedirect());
            if(type==MenuTypeEnum.不可见菜单){
                menuTree.getMeta().setShow(false);
            }else{
                menuTree.setParentId(menu.getParent()==null?"0":menu.getParent()+"");
                menuTree.getMeta().setShow(true);
            }
            menuTree.getMeta().setTitle(menu.getName());
            menuTree.getMeta().setIcon(menu.getIcon());
            menuTree.getMeta().setShow(true);
            menuTree.getMeta().setPermission(menuCode);
            menuTree.setComponent(menu.getUrl());
            toList.add(menuTree);
        }
        return toList;
    }

    @RequestMapping("/getMenuNav")
    public List<NavMenuTree> getMenuNav(Long roleId) {
                List<NavMenuTree> list;
                List<Menu> menus;
                Role role = roleService.getById(roleId);
                String roleCode = role.getCode();
                if(roleCode.equals("admin")) {
                    menus = menuService.getMenusByAdmin();
                    list=convertMenu(menus);
                }else{
                    Long userId = userRoleService.getUserIdsByRoleId(roleId);
                    //分配给角色的菜单集合
                    menus = menuService.getMenusByRoleId(roleId);
                    //分配给用户的菜单集合
                    List<Menu> menuList = menuService.getMenusByUserId(userId);
                    //去重
                    for (int i = 0; i < menuList.size(); i++) {
                        Menu menu =  menuList.get(i);
                        if(!menus.contains(menu)){
                            menus.add(menu);
                        }
                    }
                    Collections.sort(menus);
                    list=convertMenu(menus);
                }
        return list;
    }

}
