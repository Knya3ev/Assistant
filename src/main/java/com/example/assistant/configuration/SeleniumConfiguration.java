package com.example.assistant.configuration;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.io.File;

@Configuration
public class SeleniumConfiguration {

    @Bean
    @Scope(value = "prototype")
    public WebDriver webDriver() {
        String currentDir = System.getProperty("user.dir");
        String driverDir = currentDir + "/drivers";
        String driverPath = driverDir + "/chromedriver.exe";

        File directory = new File(driverDir);
        File driverFile = new File(driverPath);

        if (!directory.exists()) {
            directory.mkdirs();
        }

        if (!driverFile.exists()) {
            WebDriverManager.chromedriver().cachePath(driverDir).setup();
        } else {
            System.setProperty("webdriver.chrome.driver", driverPath);
        }
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1900,1000");
        return new ChromeDriver(options);
    }
}
