package com.starfire.familytree.sys.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.starfire.familytree.basic.entity.AbstractEntity;
import com.starfire.familytree.enums.MenuTypeEnum;
import com.starfire.familytree.vo.MenuRightVO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *
 * </p>
 *
 * @author luzh
 * @since 2019-03-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sys_menu")
public class Menu extends AbstractEntity implements Comparable {


    private static final long serialVersionUID = 1L;

    @NotEmpty(message="编码不能为空")
    private String code;

    private String icon;

    private String idPath;

    @NotEmpty(message="名称不能为空")
    private String name;

    private String remark;

    private MenuTypeEnum type;

    @NotEmpty(message="路径不能为空")
    private String url;

    /**
     * 重定向
     */
    private String redirect;

    @JsonSerialize(using=ToStringSerializer.class)
    private Long parent;

    @TableField(exist=false)
    private String parentMenuName;

    @TableField(exist=false)
    private List<String> menuRights=new ArrayList<>();

    @Override
    public int compareTo(Object o) {
        Menu menu = (Menu) o;
        Integer orderno = menu.getOrderno();
        if(this.getOrderno()>orderno){
            return 1;
        }else if(this.getOrderno()<menu.getOrderno()){
            return -1;
        }
        return 0;
    }
}
