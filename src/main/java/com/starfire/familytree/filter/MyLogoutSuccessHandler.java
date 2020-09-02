package com.starfire.familytree.filter;

import com.starfire.familytree.response.Response;
import com.starfire.familytree.utils.JacksonUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 成功退出处理
 * @author luzh
 * @version 1.0.0
 * @ClassName MyLogoutSuccessHandler.java
 * @Description TODO
 * @createTime 2020年09月01日 11:55:00
 */
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();
        Response<Object> response = new Response<Object>();
        httpServletResponse.setContentType("text/json;charset=utf-8");
        response.setResult(principal);
        response.setMsg("已成功退出");
        String json = JacksonUtils.toString(response);
        httpServletResponse.getWriter().print(json);
    }
}
