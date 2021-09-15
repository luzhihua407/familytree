package com.starfire.familytree.web.controller;

import com.starfire.familytree.response.Response;
import com.starfire.familytree.web.service.PagePrintService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    public Response<String> printPDF() {
        pagePrintService.printPDF("http://localhost:8000/tree", new File("D://export.pdf"));
        return Response.builder().success();
    }

    /**
     * 全屏截图
     * @return
     */
    @RequestMapping(value="/fullScreenShot", method= RequestMethod.GET)
    public Response<String> fullScreenShot() {
        pagePrintService.fullScreenShot("https://new.qq.com/rain/a/20210908A0CSFQ00", new File("D://image.png"));
        return Response.builder().success();
    }
}
