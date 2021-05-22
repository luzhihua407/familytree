package com.starfire.familytree.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starfire.familytree.sys.entity.RoleMenuRight;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author luzh
 * @since 2019-03-03
 */
public interface RoleMenuRightMapper extends BaseMapper<RoleMenuRight> {

    public void deleteByMenuId(Long menuId);

    public List<Map> getCheckedMenuByRoleId(Long roleId);

    public List<String> getPermission(Long roleId);


    public void deleteByRoleMenuId(Long roleMenuId);

    public List<RoleMenuRight> getRoleMenuRight(Long menuId);
}
