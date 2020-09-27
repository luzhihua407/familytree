package com.starfire.familytree.service;

import com.starfire.familytree.folk.entity.Member;
import org.springframework.context.ApplicationEvent;

public class AddMemberEvent extends ApplicationEvent {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Member member;

    public AddMemberEvent(Member member) {
        super(member);
        this.member = member;
    }


    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
