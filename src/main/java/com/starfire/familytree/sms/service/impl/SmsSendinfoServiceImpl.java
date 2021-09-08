package com.starfire.familytree.sms.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starfire.familytree.sms.entity.SmsSendinfo;
import com.starfire.familytree.sms.mapper.SmsSendinfoMapper;
import com.starfire.familytree.sms.service.ISmsSendinfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starfire.familytree.usercenter.entity.User;
import com.starfire.familytree.vo.PageInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 *  短信管理服务实现类
 * </p>
 *
 * @author luzh
 * @since 2021-09-08
 */
@Service
public class SmsSendinfoServiceImpl extends ServiceImpl<SmsSendinfoMapper, SmsSendinfo> implements ISmsSendinfoService {

	@Override
	public void addSmsSendinfo(SmsSendinfo smsSendinfo) {
		super.baseMapper.insert(smsSendinfo);
	}

	@Override
	public PageInfo<Map<String, Object>, SmsSendinfo> page(PageInfo<Map<String, Object>, SmsSendinfo> pageInfo) {
		Map<String, Object> param = pageInfo.getParam();
		Page<SmsSendinfo> page = pageInfo.toMybatisPlusPage();
		Page<SmsSendinfo> selectPage = (Page<SmsSendinfo>) super.baseMapper.queryPage(page, param);
		pageInfo.from(selectPage);
		return pageInfo;
	}
}
