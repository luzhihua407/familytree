package com.starfire.familytree.sys.controller;

import com.starfire.familytree.response.Response;
import com.starfire.familytree.sys.entity.Role;
import com.starfire.familytree.sys.service.IMenuService;
import com.starfire.familytree.sys.service.IRoleMenuService;
import com.starfire.familytree.sys.service.IRoleService;
import com.starfire.familytree.vo.DeleteVO;
import com.starfire.familytree.vo.MenuTree;
import com.starfire.familytree.vo.PageInfo;
import com.starfire.familytree.vo.RoleMenuVO;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色操作控制器
 * </p>
 *
 * @author luzh
 * @since 2019-03-03
 */
@RestController
@RequestMapping("/role")
@Api(tags = "角色模块")
public class RoleController {

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IRoleMenuService roleMenuService;

    @Autowired
    private IMenuService menuService;

    @PostMapping("/page")
    public PageInfo<Map<String, Object>, Role> page(@RequestBody(required = false) PageInfo<Map<String, Object>, Role> page) {
        if(page==null){
            page=new PageInfo<>();
        }
        PageInfo<Map<String, Object>, Role> pageInfo = roleService.page(page);
        return pageInfo;
    }

    /**
     * 新增或修改
     *
     * @param role
     * @return
     * @author luzh
     */
    @PostMapping("/addOrUpdate")
    public Response<String> addOrUpdateRole(@RequestBody @Valid Role role) {
        roleService.saveOrUpdate(role);
        List<String> menus = role.getMenus();
        RoleMenuVO roleMenuVO = new RoleMenuVO();
        roleMenuVO.setRoleId(role.getId()+"");
        roleMenuVO.setMenuIds(menus);
        roleMenuService.addOrUpdateRoleMenu(roleMenuVO);
        Response<String> response = new Response<String>();
        return response.success();

    }

    /**
     * 删除
     *
     * @return
     * @author luzh
     */
    @PostMapping("/delete")
    public Response<String> deleteRole(@RequestBody  DeleteVO<Long[]> deleteVO) {
        Long[] ids = deleteVO.getIds();
        boolean flag = roleService.removeByIds(Arrays.asList(ids));
        Response<String> response = new Response<String>();
        if (!flag) {
            return response.failure();
        }
        return response.success();

    }

    @GetMapping("/get")
    public Response<Role> getRole(Long id) {
        Role role = roleService.getById(id);
        List<String> menus = menuService.getMenusIdByRoleId(role.getId());
        role.setMenus(menus);
        role.setCheckedTree(menus);
        Response<Role> response = new Response<Role>();
        return response.success(role);

    }

    /**
     * 获取所有角色
     * @return
     */
    @GetMapping("/getRoles")
    public Response<List<Role>> getRoles() {
        List<Role> roles = roleService.getRoles();
        Response<List<Role>> response = new Response<List<Role>>();
        return response.success(roles);
    }
}
