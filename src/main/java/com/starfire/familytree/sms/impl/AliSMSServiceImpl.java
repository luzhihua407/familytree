package com.starfire.familytree.sms.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.starfire.familytree.sms.ISMSService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author luzh
 * @version 1.0.0
 * @ClassName SMSServiceImpl.java
 * @Description 阿里云短信服务实现
 * @createTime 2021年09月08日 08:47:00
 */
@Slf4j
@Service
public class AliSMSServiceImpl implements ISMSService {

	@Value("${aliYun.SMS.accessKeyId!}")
	private String accessKeyId;

	@Value("${aliYun.SMS.accessSecret!}")
	private String accessSecret;

	@Value("${aliYun.SMS.regionId!}")
	private String regionId;

	@Override
	public CommonResponse send(String phoneNum, String message){
		CommonResponse response=null;
		//初始化acsClient，<accessKeyId>和<accessSecret>"在短信控制台查询即可
		DefaultProfile profile = DefaultProfile.getProfile(regionId, accessKeyId, accessSecret);
		IAcsClient client = new DefaultAcsClient(profile);
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		//域名，请勿修改
		request.setSysDomain("dysmsapi.aliyuncs.com");
		//API版本号，请勿修改
		request.setSysVersion("2017-05-25");
		//API名称
		request.setSysAction("SendMessageToGlobe");
		//接收号码，格式为：国际码+号码，必填
		request.putQueryParameter("To", phoneNum);
		//发送方senderId，选填
		//request.putQueryParameter("From", "1234****90");
		//短信内容，必填
		request.putQueryParameter("Message", message);
		try {
			response = client.getCommonResponse(request);
			log.info(response.getData());
		} catch (ServerException e) {
			log.error(e.getMessage(),e);
		} catch (ClientException e) {
			log.error(e.getMessage(),e);
		}
		return response;
	}

}
