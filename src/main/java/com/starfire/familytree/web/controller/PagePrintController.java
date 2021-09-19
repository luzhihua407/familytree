package com.starfire.familytree.web.controller;

import com.starfire.familytree.response.Response;
import com.starfire.familytree.usercenter.entity.User;
import com.starfire.familytree.web.service.PagePrintParam;
import com.starfire.familytree.web.service.PagePrintService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.security.Principal;

@RestController
@RequestMapping("/pagePrint")
@Api(tags = "页面打印接口")
@Slf4j
public class PagePrintController {


    @Autowired
    private PagePrintService pagePrintService;

    /**
     * 页面打印PDF
     * @return
     */
    @RequestMapping(value="/printPDF", method= RequestMethod.GET)
    public Response<String> printPDF(WebRequest request) {
        Principal userPrincipal = request.getUserPrincipal();
        PagePrintParam pagePrintParam = new PagePrintParam();
        if(userPrincipal instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken auth=(UsernamePasswordAuthenticationToken)userPrincipal;
            User user=(User)auth.getPrincipal();
            pagePrintParam.setUsername(user.getUsername());
            pagePrintParam.setPassword(user.getPassword());
        }
        pagePrintParam.setLoginURL("http://localhost:8000/user/login");
        pagePrintParam.setUrl("http://localhost:8000/tree");
        pagePrintParam.setToFile(new File("D://export.pdf"));
        pagePrintService.printPDF(pagePrintParam);
        return Response.builder().success();
    }

    /**
     * 全屏截图
     * @return
     */
    @RequestMapping(value="/fullScreenShot", method= RequestMethod.GET)
    public Response<String> fullScreenShot(WebRequest request) {
        Principal userPrincipal = request.getUserPrincipal();
        PagePrintParam pagePrintParam = new PagePrintParam();
        if(userPrincipal instanceof UsernamePasswordAuthenticationToken){
            UsernamePasswordAuthenticationToken auth=(UsernamePasswordAuthenticationToken)userPrincipal;
            User user=(User)auth.getPrincipal();
            pagePrintParam.setUsername(user.getUsername());
            pagePrintParam.setPassword(user.getPassword());
        }
        pagePrintParam.setLoginURL("http://localhost:8000/user/login");
        pagePrintParam.setUrl("http://localhost:8000/tree");
        pagePrintParam.setToFile(new File("D://image.png"));
        pagePrintService.fullScreenShot(pagePrintParam);
        return Response.builder().success();
    }
}
