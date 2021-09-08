package com.starfire.familytree.sms;

import com.aliyuncs.CommonResponse;

/**
 * @author luzh
 * @version 1.0.0
 * @ClassName ISMSService.java
 * @Description 短信接口
 * @createTime 2021年09月08日 08:46:00
 */
public interface ISMSService {

	/**
	 * 发送短信
	 * @param phoneNum 手机号码
	 * @param message 短信内容
	 * @return
	 */
	public CommonResponse send(String phoneNum, String message);


}
