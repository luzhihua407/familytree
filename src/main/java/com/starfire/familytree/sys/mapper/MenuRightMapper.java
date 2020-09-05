package com.starfire.familytree.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starfire.familytree.sys.entity.MenuRight;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author luzh
 * @since 2019-03-03
 */
public interface MenuRightMapper extends BaseMapper<MenuRight> {

    public List<String> getPermissionForAdmin();
}
