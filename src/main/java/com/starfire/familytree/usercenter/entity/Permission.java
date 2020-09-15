package com.starfire.familytree.usercenter.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 按钮权限对象
 * @author luzh
 * @version 1.0.0
 * @ClassName Permission.java
 * @Description TODO
 * @createTime 2020年09月15日 12:19:00
 */
@Data
public class Permission implements Serializable {

    /**
     * 权限ID
     */
    private String permissionId;
    /**
     * 权限名称
     */
    private String permissionName;

    /**
     * 可以访问的按钮ID集合
     */
    private List<String> actionList=new ArrayList<>();
}
