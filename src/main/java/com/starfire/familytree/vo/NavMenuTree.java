package com.starfire.familytree.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 导航菜单树
 */
@Data
public class NavMenuTree {

	private String name;
	private String parentId;
	private String id;
	private NavMeta meta=new NavMeta();
	private String redirect;
	private String component;

}
