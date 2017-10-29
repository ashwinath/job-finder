package com.ashwinchat.jobfinder.factory;

import java.util.Objects;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class SeleniumDriverFactory {
    private static SeleniumDriverFactory instance = null;

    private SeleniumDriverFactory() {
        System.setProperty("webdriver.chrome.driver", "/chromedriver.exe");
    }

    public static SeleniumDriverFactory getInstance() {
        if (Objects.isNull(instance)) {
            instance = new SeleniumDriverFactory();
        }

        return instance;
    }

    public WebDriver createChromeWebDriver() {
        final ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        return new ChromeDriver(chromeOptions);
    }

}
