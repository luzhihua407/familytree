package com.starfire.familytree.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starfire.familytree.sys.entity.UserRole;
import com.starfire.familytree.sys.mapper.UserRoleMapper;
import com.starfire.familytree.sys.service.IUserRoleService;
import com.starfire.familytree.vo.PageInfo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author luzh
 * @since 2019-03-03
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {
    @Override
    public PageInfo<Map<String, Object>, UserRole> page(PageInfo<Map<String, Object>, UserRole> pageInfo) {
        QueryWrapper<UserRole> qw = new QueryWrapper<UserRole>();
        Page<UserRole> page = pageInfo.toMybatisPlusPage();
        Page<UserRole> selectPage = (Page<UserRole>) baseMapper.selectPage(page, qw);
        pageInfo.from(selectPage);
        return pageInfo;
    }

    @Override
    public Long getRoleIdByUserId(Long userId) {
        return baseMapper.getRoleIdByUserId(userId);
    }

    @Override
    public List<Long> getRoleIdsByUserId(Long userId) {
        return baseMapper.getRoleIdsByUserId(userId);
    }

    @Override
    public void deleteRoleByUserId(Long userId) {
        baseMapper.deleteRoleByUserId(userId);
    }

//    @Override
//    public Long getUserIdsByRoleId(Long roleId) {
//        return baseMapper.getUserIdsByRoleId(roleId);
//    }
}
