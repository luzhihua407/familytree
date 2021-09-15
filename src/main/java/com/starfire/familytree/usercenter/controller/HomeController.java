package com.starfire.familytree.usercenter.controller;

import com.starfire.familytree.web.service.WebService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;


@Controller
@Api(tags = "登录接口")
public class HomeController {


    @Value("${web.loginPage}")
    private String loginPage ;

    @Autowired
    private WebService webService;

    @RequestMapping(value="/login", method= RequestMethod.GET)
    public String login() {

        return "redirect:loginPage";
    }

    @RequestMapping(value="/index", method= RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value="/test", method= RequestMethod.GET)
    @ResponseBody
    public String test() {
//        webService.fullScreenShot("https://new.qq.com/rain/a/20210908A0CSFQ00", new File("D://image.png"));
        webService.printPDF("http://www.baidu.com", new File("D://export.pdf"));
        return "index";
    }
}
