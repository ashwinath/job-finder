package com.ashwinchat.jobfinder.scraping.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ashwinchat.jobfinder.scraping.ScrapingStrategy;
import com.ashwinchat.jobfinder.selenium.SeleniumDriver;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public class TechinAsiaScrapingStrategy implements ScrapingStrategy {

    private static final String BASE_URL_FORMAT = "https://www.techinasia.com/jobs?job_category_name[]=Web%20Development&job_category_name[]=Enterprise%20Software%20&%20Systems&job_type_name[]=Full-time&location_name[]=Singapore&page=";
    private WebDriver webDriver = SeleniumDriver.getInstance().getDriver();
    private static final String AGENCY_NAME = "TechInAsia";

    private static final int NORMAL_SCENARIO = 0;
    private static final int NOT_YEARS_EXP_REQUIRED = 1;
    private static final int LESS_THAN_1_YEAR = 2;

    @Override
    public List<ScrapedInfo> scrape() {
        List<String> allLinks = this.findAllJobLinksInOnePage(BASE_URL_FORMAT + 1);
        List<ScrapedInfo> firstPageScrapedInfo = allLinks.stream().map(this::getJobInfoFromAPage)
                .collect(Collectors.toList());
        return firstPageScrapedInfo;
    }

    private List<String> findAllJobLinksInOnePage(String url) {
        webDriver.get(url);
        List<WebElement> searchResultWebElement = webDriver.findElements(By.className("search-results__item"));
        return searchResultWebElement.stream()
                .map(element -> element.findElement(By.className("job-listing")).getAttribute("href"))
                .collect(Collectors.toList());
    }

    private ScrapedInfo getJobInfoFromAPage(String url) {
        ScrapedInfo scrapedInfo = new ScrapedInfo();
        scrapedInfo.setAgency(AGENCY_NAME);
        scrapedInfo.setUrl(url);

        webDriver.get(url);

        String title = webDriver.findElement(By.className("entity__name")).getText();
        scrapedInfo.setTitle(title);

        String companyName = webDriver.findElement(By.cssSelector(".entity__type a")).getText();
        scrapedInfo.setCompanyName(companyName);

        // panels
        List<WebElement> panels = webDriver.findElements(By.className("panel"));

        // Brief panel
        WebElement briefPanel = panels.get(1);
        List<WebElement> allParagraphTags = briefPanel.findElements(By.cssSelector("p"));

        String salary = allParagraphTags.get(2).getText();
        this.setSalary(salary, scrapedInfo);

        String yearsExperience = allParagraphTags.get(3).getText();
        this.setYearsExperience(yearsExperience, scrapedInfo);

        // job descr panel
        WebElement jdPanel = panels.get(2);
        String jobDescr = jdPanel.findElement(By.cssSelector(".panel-body div")).getText();
        scrapedInfo.setJobDescr(jobDescr);

        scrapedInfo.setCreOn(LocalDateTime.now());
        return scrapedInfo;
    }

    private void setSalary(String salary, ScrapedInfo scrapedInfo) {
        if (StringUtils.contains(salary, "Salary Range")) {
            String[] stringSplit = this.splitParagraphTag(salary);
            scrapedInfo.setPayMin(new BigDecimal(stringSplit[0]));
            scrapedInfo.setPayMax(new BigDecimal(stringSplit[1]));
        }
    }

    private void setYearsExperience(String yearsExperience, ScrapedInfo scrapedInfo) {
        int yearExpResult = this.checkYearsExperience(yearsExperience);
        if (yearExpResult == NORMAL_SCENARIO) {
            String[] yearsExperienceSplit = this.splitParagraphTag(yearsExperience);
            if (yearsExperienceSplit.length > 0) {
                scrapedInfo.setExpMin(new BigDecimal(yearsExperienceSplit[0]));
                scrapedInfo.setExpMax(new BigDecimal(yearsExperienceSplit[1]));
            }
        } else if (yearExpResult == LESS_THAN_1_YEAR) {
            scrapedInfo.setExpMin(BigDecimal.ZERO);
            scrapedInfo.setExpMax(BigDecimal.ONE);
        }
    }

    private int checkYearsExperience(String yearsExperience) {
        if (!StringUtils.contains(yearsExperience, "Years of Experience Required")) {
            return NOT_YEARS_EXP_REQUIRED;
        } else if (StringUtils.contains(yearsExperience, "Less than 1 year")) {
            return LESS_THAN_1_YEAR;
        }
        return NORMAL_SCENARIO;
    }

    private String[] splitParagraphTag(String string) {
        String replacedDeceivingHypenString = StringUtils.replace(string, "–", "-");
        String strippedOfAlphabet = replacedDeceivingHypenString.replaceAll("[^\\d-]", "");
        return StringUtils.split(strippedOfAlphabet, "-");
    }

}
