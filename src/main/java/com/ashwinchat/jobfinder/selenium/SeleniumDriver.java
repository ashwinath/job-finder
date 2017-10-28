package com.ashwinchat.jobfinder.selenium;

import java.util.Objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumDriver {
    private static SeleniumDriver instance = null;
    private final WebDriver driver;

    private SeleniumDriver() {
        System.setProperty("webdriver.chrome.driver", "/chromedriver.exe");
        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        this.driver = new ChromeDriver(chromeOptions);
    }

    public static SeleniumDriver getInstance() {
        if (Objects.isNull(instance)) {
            instance = new SeleniumDriver();
        }

        return instance;
    }

    public WebDriver getDriver() {
        return this.driver;
    }

}
