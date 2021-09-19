package com.starfire.familytree.filter;

import com.starfire.familytree.config.SecurityConfig;
import com.starfire.familytree.response.Response;
import com.starfire.familytree.sys.service.*;
import com.starfire.familytree.usercenter.entity.RoleVO;
import com.starfire.familytree.usercenter.entity.User;
import com.starfire.familytree.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 认证成功处理
 * @author luzh
 * @version 1.0.0
 * @ClassName MyAuthenticationSuccessHandler.java
 * @Description TODO
 * @createTime 2020年09月01日 11:24:00
 */
@Component
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Autowired
    private IPermissionService permissionService;

    @Autowired
    private CacheManager cacheManager;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authentication);
        HttpSession session = httpServletRequest.getSession(true);
        log.debug("create sessionId={}",session.getId());
        session.setAttribute(SecurityConfig.JSESSIONID, sc);
        Object principal = authentication.getPrincipal();
        if(principal instanceof User){
            User user=(User) principal;
            Long id = user.getId();
            RoleVO roleVO = permissionService.authorization(id);
            user.setRole(roleVO);
            cacheManager.getCache("users").put(id,user.getPassword());
        }
        Response<Object> response = new Response<Object>();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        response.setResult(principal);
        String json = JacksonUtils.toString(response);
        httpServletResponse.getWriter().print(json);
    }

}
