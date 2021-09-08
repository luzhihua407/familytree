package com.starfire.familytree.sms.controller;


import com.starfire.familytree.response.Response;
import com.starfire.familytree.sms.entity.SmsSendinfo;
import com.starfire.familytree.sms.service.ISmsSendinfoService;
import com.starfire.familytree.vo.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author luzh
 * @since 2021-09-08
 */
@RestController
@RequestMapping("/sms")
public class SmsSendinfoController {

	@Autowired
	private ISmsSendinfoService smsSendinfoService;

	/**
	 * 分页
	 *
	 * @param page
	 * @return
	 * @author luzh
	 */
	@PostMapping("/page")
	public Response page(@RequestBody(required = false) PageInfo<Map<String, Object>, SmsSendinfo> page) {
		page=page==null?new PageInfo<>():page;
		PageInfo<Map<String, Object>, SmsSendinfo> pageInfo = smsSendinfoService.page(page);
		return Response.builder().success(pageInfo);

	}

	@PostMapping("/add")
	public Response addCategoryContent(@RequestBody SmsSendinfo smsSendinfo) {
		smsSendinfoService.addSmsSendinfo(smsSendinfo);
		return Response.builder().success();
	}
}
