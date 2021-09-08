package com.starfire.familytree.sms.service;

import com.starfire.familytree.sms.entity.SmsSendinfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starfire.familytree.usercenter.entity.User;
import com.starfire.familytree.vo.PageInfo;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author luzh
 * @since 2021-09-08
 */
public interface ISmsSendinfoService extends IService<SmsSendinfo> {

	public void addSmsSendinfo(SmsSendinfo smsSendinfo);

	public PageInfo<Map<String, Object>, SmsSendinfo> page(PageInfo<Map<String, Object>, SmsSendinfo> pageInfo);

}
