package com.starfire.familytree.web.service.impl;


import com.starfire.familytree.web.service.WebService;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
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

@Slf4j
@Service
public class WebServiceImpl implements WebService {

    @Value("${selenium.webDriverPath}")
    private String webDriverPath;
    @Override
    public void fullScreenShot(String url, File toFile) {
        FileOutputStream fileOutputStream=null;
        try {
            System.setProperty("webdriver.chrome.driver", webDriverPath);
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
            chromeOptions.setHeadless(true);
//            chromeOptions.addArguments("window-size=1920x1080");
            ChromeDriver driver = new ChromeDriver(chromeOptions);
//        WebDriver driver = new ChromeDriver();
//            WebDriverWait wait = new WebDriverWait(driver, 30);
            driver.get(url);
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
        }finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void printPDF(String url, File toFile) {
        System.setProperty("webdriver.chrome.driver", webDriverPath);
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        chromeOptions.setHeadless(true);
        ChromeDriver driver = new ChromeDriver(chromeOptions);
        driver.get(url);
        FileOutputStream fileOutputStream=null;
        try {
        String command = "Page.printToPDF";
        Map<String, Object> params = new HashMap<>();
        params.put("landscape", false);
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
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        driver.quit();
    }
}
