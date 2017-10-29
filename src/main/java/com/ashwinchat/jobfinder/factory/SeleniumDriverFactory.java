package com.ashwinchat.jobfinder.factory;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import com.ashwinchat.jobfinder.constants.Constants;
import com.ashwinchat.jobfinder.database.DatabaseUtil;
import com.ashwinchat.jobfinder.view.SystemConfig;

public class SeleniumDriverFactory {
    private static SeleniumDriverFactory instance = null;
    private Logger logger = Logger.getLogger(this.getClass().getName());

    private SeleniumDriverFactory() {
        if (SystemUtils.IS_OS_WINDOWS) {
            System.setProperty("webdriver.chrome.driver", "./chromedriver.exe");
        } else if (SystemUtils.IS_OS_MAC) {
            System.setProperty("webdriver.chrome.driver", "./chromedriver");
        }
    }

    public static SeleniumDriverFactory getInstance() {
        if (Objects.isNull(instance)) {
            instance = new SeleniumDriverFactory();
        }

        return instance;
    }

    public WebDriver createChromeWebDriver() {
        return new ChromeDriver(this.checkHeadlessCondition());
    }

    private ChromeOptions checkHeadlessCondition() {
        final ChromeOptions chromeOptions = new ChromeOptions();
        try {
            List<SystemConfig> config = DatabaseUtil.getSystemConfigValue(Constants.SYS_CD_SELENIUM,
                    Constants.KEY_HEADLESS);
            if (CollectionUtils.isNotEmpty(config)) {
                String headlessInd = config.get(0).getValue();
                if (StringUtils.equals(StringUtils.upperCase(headlessInd), Constants.VALUE_TRUE)) {
                    chromeOptions.addArguments("--headless");
                }
            }
        } catch (Exception e) {
            logger.severe(ExceptionUtils.getRootCauseMessage(e));
            logger.severe(Constants.ERROR_DATABASE_CONNECT + System.getProperty(Constants.DATABASE_LOCATION_PROPERTY));
            // No Database connection, no point continuing
            System.exit(-1);
        }

        return chromeOptions;
    }

}
