package com.starfire.familytree.usercenter.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色值对象
 * @author luzh
 * @version 1.0.0
 * @ClassName RoleVO.java
 * @Description TODO
 * @createTime 2020年09月15日 12:17:00
 */
@Data
public class RoleVO implements Serializable {

    /**
     * 角色编号
     */
    private String id;
    /**
     * 角色名称
     */
    private String name;

    private List<Permission> permissions=new ArrayList<>();
}
