package com.starfire.familytree.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starfire.familytree.sys.entity.MenuRight;
import com.starfire.familytree.sys.mapper.MenuRightMapper;
import com.starfire.familytree.sys.service.IMenuRightService;
import com.starfire.familytree.vo.MenuRightVO;
import com.starfire.familytree.vo.PageInfo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
public class MenuRightServiceImpl extends ServiceImpl<MenuRightMapper, MenuRight> implements IMenuRightService {
    @Override
    public PageInfo<Map<String, Object>, MenuRight> page(PageInfo<Map<String, Object>, MenuRight> pageInfo) {
        QueryWrapper<MenuRight> qw = new QueryWrapper<MenuRight>();
        Map<String, Object> param = pageInfo.getParam();
        Page<MenuRight> page = pageInfo.toMybatisPlusPage();
        Page<MenuRight> selectPage = (Page<MenuRight>) baseMapper.selectPage(page, qw);
        pageInfo.from(selectPage);
        return pageInfo;
    }

    @Override
    public List<MenuRight> getList(Long menuId) {
        QueryWrapper<MenuRight> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("menu_id",menuId);
        List<MenuRight> menuRights = baseMapper.selectList(queryWrapper);
        return menuRights;
    }

    @Override
    public List<MenuRightVO> convert(List<MenuRight> menuRights) {
        List<MenuRightVO> list=new ArrayList<>();
        for (int i = 0; i < menuRights.size(); i++) {
            MenuRight menuRight =  menuRights.get(i);
            MenuRightVO vo=new MenuRightVO();
            vo.setValue(menuRight.getMenuId()+"");
            vo.setKey(menuRight.getMenuId()+"");
            vo.setLabel(menuRight.getName());
            list.add(vo);
        }
        return list;
    }

    @Override
    public List<String> convertStringList(List<MenuRight> menuRights) {
        List<String> list=new ArrayList<>();
        for (int i = 0; i < menuRights.size(); i++) {
            MenuRight menuRight =  menuRights.get(i);
            list.add(menuRight.getCode());
        }
        return list;
    }

    @Override
    public void removeByMenuId(Long menuId) {
        QueryWrapper<MenuRight> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("menu_id",menuId);
        baseMapper.delete(queryWrapper);
    }

    @Override
    public List<String> getPermissionForAdmin() {
        return baseMapper.getPermissionForAdmin();
    }


}
