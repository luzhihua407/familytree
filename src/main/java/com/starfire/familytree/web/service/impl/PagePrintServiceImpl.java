package com.starfire.familytree.web.service.impl;


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
    public void fullScreenShot(String host,String sessionId,String url, File toFile) {
        try {
            System.setProperty("webdriver.chrome.driver", webDriverPath);
            ChromeOptions chromeOptions = new ChromeOptions();
//            chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            chromeOptions.setHeadless(false);
//            chromeOptions.addArguments("window-size=1920x1080");
            ChromeDriver driver = new ChromeDriver(chromeOptions);
//        WebDriver driver = new ChromeDriver();
//            WebDriverWait wait = new WebDriverWait(driver, 30);
            driver.get(url);
            driver.manage().addCookie(new Cookie.Builder("domain", host).sameSite("Lax").build());
            driver.manage().addCookie(new Cookie.Builder("SESSION", sessionId).sameSite("Lax").build());
            // Get All available cookies
            Set<Cookie> cookies = driver.manage().getCookies();
            System.out.println(cookies);
//            WebElement myWebElement = driver.findElement(By.className("content-article"));
//            driver.executeScript("document.body.style.zoom='60%'");
//            WebElement html = driver.findElement(By.tagName("html"));
//            html.sendKeys(Keys.chord(Keys.CONTROL, "0"));
//            html.sendKeys(Keys.chord(Keys.CONTROL, Keys.SUBTRACT));
//            Thread.sleep(2000);
//        File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
            Screenshot screenshot = new AShot().
                    shootingStrategy(ShootingStrategies.viewportPasting(100)).
                    takeScreenshot(driver);
            driver.executeScript("document.body.style.overflow = 'hidden';");
//        FileUtils.copyFile(scrFile, new File("d://image1.png"));
            ImageIO.write(screenshot.getImage(), "PNG", toFile);
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
    public void printPDF(String host,String sessionId,String url, File toFile) {
        System.setProperty("webdriver.chrome.driver", webDriverPath);
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        chromeOptions.setHeadless(true);
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        driver.get("http://localhost:8000/user/login");
        driver.findElement(By.id("username")).sendKeys("member");
        driver.findElement(By.id("password")).sendKeys("123456");
        driver.findElement(By.className("login-button")).click();
        driver.manage().addCookie(new Cookie("key", "value"));
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(3));
        try {
            Thread.sleep(2000);
        driver.get(url);
            Thread.sleep(4000);
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
            fileOutputStream = new FileOutputStream(toFile);
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
