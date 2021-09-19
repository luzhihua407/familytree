package com.starfire.familytree.web.service.impl;


import com.starfire.familytree.config.SecurityConfig;
import com.starfire.familytree.web.service.PagePrintParam;
import com.starfire.familytree.web.service.PagePrintService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class PagePrintServiceImpl implements PagePrintService {

    @Value("${selenium.webDriverPath}")
    private String webDriverPath;
    @Override
    public void fullScreenShot(PagePrintParam pagePrintParam) {
        try {
            System.setProperty("webdriver.chrome.driver", webDriverPath);
            ChromeOptions chromeOptions = new ChromeOptions();
//            chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            chromeOptions.setHeadless(true);
//            chromeOptions.addArguments("window-size=1920x1080");
            ChromeDriver driver = new ChromeDriver(chromeOptions);
//        WebDriver driver = new ChromeDriver();
//            WebDriverWait wait = new WebDriverWait(driver, 30);
            driver.get(pagePrintParam.getLoginURL());
            driver.findElement(By.id("username")).sendKeys(pagePrintParam.getUsername());
            driver.findElement(By.id("password")).sendKeys(pagePrintParam.getPassword());
            driver.findElement(By.className("login-button")).click();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            driver.getLocalStorage().setItem("Access-Token",pagePrintParam.getUserId()+"");
//            driver.get(pagePrintParam.getUrl());
//            driver.manage().addCookie(new Cookie.Builder("domain", pagePrintParam.getHost()).sameSite("Lax").build());
//            driver.manage().addCookie(new Cookie.Builder(SecurityConfig.JSESSIONID, sessionId).sameSite("Lax").build());
            // Get All available cookies
//            WebElement myWebElement = driver.findElement(By.className("content-article"));
//            driver.executeScript("document.body.style.zoom='60%'");
//            WebElement html = driver.findElement(By.tagName("html"));
//            html.sendKeys(Keys.chord(Keys.CONTROL, "0"));
//            html.sendKeys(Keys.chord(Keys.CONTROL, Keys.SUBTRACT));

//        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            Screenshot screenshot = new AShot().
                    shootingStrategy(ShootingStrategies.viewportPasting(100)).
                    takeScreenshot(driver);
            driver.executeScript("document.body.style.overflow = 'hidden';");
//        FileUtils.copyFile(scrFile, new File("d://image1.png"));
            ImageIO.write(screenshot.getImage(), "PNG", pagePrintParam.getToFile());
//            Map<String, Object> output = driver.executeCdpCommand("Page.captureScreenshot", new HashMap<>());
//            fileOutputStream = new FileOutputStream(toFile);
//            byte[] byteArray = Base64.getDecoder().decode((String)output.get("data"));
//            fileOutputStream.write(byteArray);
            driver.quit();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void printPDF(PagePrintParam pagePrintParam) {
        System.setProperty("webdriver.chrome.driver", webDriverPath);
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        chromeOptions.setHeadless(true);
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        driver.get(pagePrintParam.getLoginURL());
        driver.findElement(By.id("username")).sendKeys(pagePrintParam.getUsername());
        driver.findElement(By.id("password")).sendKeys(pagePrintParam.getPassword());
        driver.findElement(By.className("login-button")).click();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FileOutputStream fileOutputStream=null;
        try {
        String command = "Page.printToPDF";
        Map<String, Object> params = new HashMap<>();
        params.put("landscape", true);
        params.put("printBackground", true);
        params.put("preferCSSPageSize", true);
        Map<String, Object> output = driver.executeCdpCommand(command, params);
            fileOutputStream = new FileOutputStream(pagePrintParam.getToFile());
            byte[] byteArray = Base64.getDecoder().decode((String)output.get("data"));
            fileOutputStream.write(byteArray);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if(fileOutputStream!=null){

                fileOutputStream.close();
                }
                 driver.quit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
