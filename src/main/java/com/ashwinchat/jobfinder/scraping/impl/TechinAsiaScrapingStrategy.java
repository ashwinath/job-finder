package com.ashwinchat.jobfinder.scraping.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.ashwinchat.jobfinder.constants.Constants;
import com.ashwinchat.jobfinder.database.DatabaseUtil;
import com.ashwinchat.jobfinder.factory.SeleniumDriverFactory;
import com.ashwinchat.jobfinder.scraping.ScrapingStrategy;
import com.ashwinchat.jobfinder.view.ScrapedInfo;
import com.ashwinchat.jobfinder.view.SystemConfig;

public class TechinAsiaScrapingStrategy implements ScrapingStrategy {

    private static final String BASE_URL_FORMAT = "https://www.techinasia.com/jobs?job_category_name%5B%5D=Web%20Development&job_category_name%5B%5D=Enterprise%20Software%20%26%20Systems&job_type_name%5B%5D=Full-time&location_name%5B%5D=Singapore&page=";
    private static final String AGENCY_NAME = Constants.TECH_IN_ASIA_NAME;
    private static final int NORMAL_SCENARIO = 0;
    private static final int NOT_YEARS_EXP_REQUIRED = 1;
    private static final int LESS_THAN_X_YEARS = 2;
    private static final int MORE_THAN_X_YEARS = 3;

    private WebDriver webDriver = SeleniumDriverFactory.getInstance().createChromeWebDriver();
    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String baseUrl;

    public TechinAsiaScrapingStrategy() {
        try {
            List<SystemConfig> configs = DatabaseUtil.getSystemConfigValue(Constants.SYS_CD_URL,
                    Constants.KEY_TECH_IN_ASIA);
            if (CollectionUtils.isNotEmpty(configs)) {
                this.baseUrl = configs.get(0).getValue();
            } else {
                this.baseUrl = BASE_URL_FORMAT;
            }

        } catch (Exception e) {
            this.logger.severe(ExceptionUtils.getRootCauseMessage(e));
            this.logger.severe(
                    Constants.ERROR_DATABASE_CONNECT + System.getProperty(Constants.DATABASE_LOCATION_PROPERTY));
            // No Database connection, no point continuing
            System.exit(-1);
        }
    }

    @Override
    public List<ScrapedInfo> scrape() {
        try {
            List<String> allLinks = this.findAllJobLinks();
            return allLinks.stream().map(this::getJobInfoFromAPage).collect(Collectors.toList());
        } finally {
            this.webDriver.close();
        }
    }

    private List<String> findAllJobLinks() {
        List<String> allLinks = new ArrayList<>();
        for (int i = 1;; ++i) {
            this.webDriver.get(baseUrl + i);
            if (CollectionUtils.isEmpty(this.webDriver.findElements(By.className("alert")))) {

                List<WebElement> searchResultWebElement = this.webDriver
                        .findElements(By.className("search-results__item"));

                List<String> linksOnThisPage = searchResultWebElement.stream()
                        .map(element -> element.findElement(By.className("job-listing")).getAttribute("href"))
                        .collect(Collectors.toList());

                allLinks.addAll(linksOnThisPage);
            } else {
                break;
            }
        }
        return allLinks;
    }

    private ScrapedInfo getJobInfoFromAPage(String url) {
        try {
            ScrapedInfo scrapedInfo = new ScrapedInfo();
            scrapedInfo.setAgency(AGENCY_NAME);
            scrapedInfo.setUrl(url);

            this.webDriver.get(url);

            // Sometimes there's an error loading the page, we wait for the site to reload
            // (30s auto refresh).
            Wait<WebDriver> waitingDriver = new FluentWait<>(this.webDriver).withTimeout(30, TimeUnit.SECONDS)
                    .pollingEvery(200, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);
            String title = waitingDriver.until(driver -> driver.findElement(By.className("entity__name")).getText());

            scrapedInfo.setTitle(title);

            String companyName = this.webDriver.findElement(By.cssSelector(".entity__type a")).getText();
            scrapedInfo.setCompanyName(companyName);

            // panels
            List<WebElement> panels = this.webDriver.findElements(By.className("panel"));

            // Brief panel
            WebElement briefPanel = panels.get(1);

            // factor out
            this.getSalaryAndExp(briefPanel, scrapedInfo);

            // job descr panel
            WebElement jdPanel = panels.get(2);
            String jobDescr = jdPanel.findElement(By.cssSelector(".panel-body div")).getText();
            scrapedInfo.setJobDescr(jobDescr);

            scrapedInfo.setCreOn(LocalDateTime.now());
            return scrapedInfo;
        } catch (Exception e) {
            this.logger.warning("url = " + url + ", " + ExceptionUtils.getStackTrace(e));
        }

        return new ScrapedInfo();
    }

    private void getSalaryAndExp(WebElement briefPanel, ScrapedInfo scrapedInfo) {
        String briefPanelText = briefPanel.findElement(By.className("panel-body")).getText();
        List<String> paragraphs = Arrays.asList(StringUtils.split(briefPanelText, "\n"));

        String salary = this.filterByText(paragraphs, "Salary Range");
        this.setSalary(salary, scrapedInfo);

        String yearsExperience = this.filterByText(paragraphs, "Years of Experience Required");
        this.setYearsExperience(yearsExperience, scrapedInfo);

    }

    private String filterByText(List<String> paragraphs, String filterText) {
        return paragraphs.stream().filter(string -> StringUtils.contains(string, filterText)).findFirst().orElse("");
    }

    private void setSalary(String salary, ScrapedInfo scrapedInfo) {
        String[] stringSplit = this.splitParagraphTag(salary);
        if (StringUtils.contains(salary, "Up to SGD")) {
            scrapedInfo.setPayMin(BigDecimal.ZERO);
            scrapedInfo.setPayMax(new BigDecimal(stringSplit[0]));
        } else if (stringSplit.length == 1) {
            // This is short circuited
            // Could be in the form of just SGD ... (single value)
            scrapedInfo.setPayMin(new BigDecimal(stringSplit[0]));
            scrapedInfo.setPayMax(new BigDecimal(stringSplit[0]));
        } else if (StringUtils.contains(salary, "Salary Range")) {
            scrapedInfo.setPayMin(new BigDecimal(stringSplit[0]));
            scrapedInfo.setPayMax(new BigDecimal(stringSplit[1]));
        }
    }

    private void setYearsExperience(String yearsExperience, ScrapedInfo scrapedInfo) {
        int yearExpResult = this.checkYearsExperience(yearsExperience);
        String[] yearsExperienceSplit = this.splitParagraphTag(yearsExperience);
        if (yearExpResult == NORMAL_SCENARIO) {
            if (yearsExperienceSplit.length > 0) {
                scrapedInfo.setExpMin(new BigDecimal(yearsExperienceSplit[0]));
                scrapedInfo.setExpMax(new BigDecimal(yearsExperienceSplit[1]));
            }
        } else if (yearExpResult == LESS_THAN_X_YEARS) {
            scrapedInfo.setExpMin(BigDecimal.ZERO);
            scrapedInfo.setExpMax(new BigDecimal(yearsExperienceSplit[0]));
        } else if (yearExpResult == MORE_THAN_X_YEARS) {
            scrapedInfo.setExpMin(new BigDecimal(yearsExperienceSplit[0]));
        }
    }

    private int checkYearsExperience(String yearsExperience) {
        if (!StringUtils.contains(yearsExperience, "Years of Experience Required")) {
            return NOT_YEARS_EXP_REQUIRED;
        } else if (StringUtils.contains(yearsExperience, "Less than")) {
            return LESS_THAN_X_YEARS;
        } else if (StringUtils.contains(yearsExperience, "More than")) {
            return MORE_THAN_X_YEARS;
        }
        return NORMAL_SCENARIO;
    }

    private String[] splitParagraphTag(String string) {
        String replacedDeceivingHypenString = StringUtils.replace(string, "–", "-");
        String strippedOfAlphabet = replacedDeceivingHypenString.replaceAll("[^\\d-]", "");
        return StringUtils.split(strippedOfAlphabet, "-");
    }

}
