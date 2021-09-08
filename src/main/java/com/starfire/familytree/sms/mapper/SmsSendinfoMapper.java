package com.starfire.familytree.sms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starfire.familytree.sms.entity.SmsSendinfo;
import com.starfire.familytree.usercenter.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author luzh
 * @since 2021-09-08
 */
public interface SmsSendinfoMapper extends BaseMapper<SmsSendinfo> {

	public IPage<SmsSendinfo> queryPage(Page page, @Param("queryCon") Map<String,Object> queryCon);
}
