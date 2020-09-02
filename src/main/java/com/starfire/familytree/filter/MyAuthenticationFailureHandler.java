package com.starfire.familytree.filter;

import com.starfire.familytree.bs.entity.Village;
import com.starfire.familytree.response.Response;
import com.starfire.familytree.utils.JacksonUtils;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 认证失败处理
 * @author luzh
 * @version 1.0.0
 * @ClassName MyAuthenticationFailureHandler.java
 * @Description TODO
 * @createTime 2020年09月01日 11:43:00
 */
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        String message = e.getMessage();
        Response<Object> response = new Response<Object>();
        response.setSuccess(false);
        response.setCode(401);
        httpServletResponse.setContentType("text/json;charset=utf-8");
        response.setMsg(message);
        String json = JacksonUtils.toString(response);
        httpServletResponse.getWriter().print(json);
    }
}
