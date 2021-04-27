package com.starfire.familytree.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * 邮件发送服务
 * @author luzh
 * @version 1.0.0
 * @ClassName EmailSender.java
 * @Description TODO
 * @createTime 2021年04月27日 15:56:00
 */
@Service
public class EmailSenderImpl implements EmailSender {

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void send(SimpleMailMessage message) {
		mailSender.send(message);
	}
}
