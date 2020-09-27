package com.starfire.familytree.listeners;

import com.starfire.familytree.folk.entity.Member;
import com.starfire.familytree.folk.service.IMemberService;
import com.starfire.familytree.service.AddMemberEvent;
import com.starfire.familytree.service.IVerificationTokenService;
import com.starfire.familytree.service.OnRegistrationCompleteEvent;
import com.starfire.familytree.usercenter.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AddMemberListener implements
        ApplicationListener<AddMemberEvent> {

    @Autowired
    private IMemberService memberService;


    @Override
    public void onApplicationEvent(AddMemberEvent addMemberEvent) {
        Member member = addMemberEvent.getMember();
        memberService.addMember(member);
    }
}
