package com.starfire.familytree.utils;

import com.starfire.familytree.usercenter.entity.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SessionHelper {

    private static User user = null;

    public SessionHelper() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if(authentication instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken auth=(UsernamePasswordAuthenticationToken)authentication;
            user=(User)  auth.getPrincipal();;
        }
    }
    public Long getUserId(){
        if(user!=null){
            return user.getId();
        }
        return null;
    }

    public User getUser(){
        return user;
    }
}
