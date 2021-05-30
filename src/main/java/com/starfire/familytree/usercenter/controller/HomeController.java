package com.starfire.familytree.usercenter.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
@Api(tags = "登录接口")
public class HomeController {


    @Value("${web.loginPage}")
    private String loginPage ;

    @RequestMapping(value="/login", method= RequestMethod.GET)
    public String login() {

        return "redirect:loginPage";
    }
}
