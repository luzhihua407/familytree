package com.starfire.familytree.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PrincipalVO {

    UserVO user;

    private List<NavMenuTree> menus=new ArrayList<NavMenuTree>();
    private List<String> permission=new ArrayList<String>();
}
