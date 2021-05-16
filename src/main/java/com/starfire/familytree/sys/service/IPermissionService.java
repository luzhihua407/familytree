package com.starfire.familytree.sys.service;

import com.starfire.familytree.usercenter.entity.RoleVO;
import com.starfire.familytree.usercenter.entity.User;

/**
 * 权限服务
 */
public interface IPermissionService {

    /**
     * 返回用户的授权信息
     * @param userId 用户编码
     * @return 返回带权限的用户
     */
    public RoleVO authorization(Long userId);

}
