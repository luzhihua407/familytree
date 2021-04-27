package com.starfire.familytree.email;

import org.springframework.mail.SimpleMailMessage;

/**
 * 邮件发送接口
 * @author luzh
 * @version 1.0.0
 * @ClassName EmailSender.java
 * @Description TODO
 * @createTime 2021年04月27日 15:57:00
 */
public interface EmailSender {

	public void send(SimpleMailMessage message);
}
