package com.starfire.familytree.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starfire.familytree.sys.entity.RoleMenu;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author luzh
 * @since 2019-03-03
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {


    public void deleteByRoleId(Long roleId);

    public List<RoleMenu> getListByRoleId(Long roleId);
}
