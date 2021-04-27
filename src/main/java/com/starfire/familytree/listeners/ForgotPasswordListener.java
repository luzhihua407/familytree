package com.starfire.familytree.listeners;

import com.starfire.familytree.email.EmailSender;
import com.starfire.familytree.service.IVerificationTokenService;
import com.starfire.familytree.service.OnForgotPasswordEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class ForgotPasswordListener implements
        ApplicationListener<OnForgotPasswordEvent> {

    @Autowired
    private EmailSender mailSender;

    @Value("${email.system}")
    private String systemEmail;

    @Override
    public void onApplicationEvent(OnForgotPasswordEvent event) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom(systemEmail);
        email.setTo(event.getEmail());
        email.setSubject("找回密码");
        email.setText("<html><body><font color='red'>测试</font></body></html>");
        mailSender.send(email);
    }

}
