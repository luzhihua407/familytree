package com.starfire.familytree.web.controller;

import com.starfire.familytree.response.Response;
import com.starfire.familytree.web.service.PagePrintService;
import io.swagger.annotations.Api;
import org.openqa.selenium.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpSession;
import java.io.File;

@RestController
@RequestMapping("/pagePrint")
@Api(tags = "页面打印接口")
public class PagePrintController {


    @Autowired
    private PagePrintService pagePrintService;

    /**
     * 页面打印PDF
     * @return
     */
    @RequestMapping(value="/printPDF", method= RequestMethod.GET)
    public Response<String> printPDF(WebRequest request) {
        String host = request.getHeader("host");
        String sessionId = request.getSessionId();
        System.err.println(host);
        System.err.println(sessionId);
        pagePrintService.printPDF(host,sessionId,"http://localhost:8000/tree", new File("D://export.pdf"));
        return Response.builder().success();
    }

    /**
     * 全屏截图
     * @return
     */
    @RequestMapping(value="/fullScreenShot", method= RequestMethod.GET)
    public Response<String> fullScreenShot(WebRequest request,@CookieValue("SESSION") String sessionId) {
        String host = request.getHeader("host");
        System.err.println(host);
        System.err.println(sessionId);
        pagePrintService.fullScreenShot(host,sessionId,"http://localhost:8000/tree", new File("D://image.png"));
        return Response.builder().success();
    }
}
