package com.ashwinchat.jobfinder.scraping.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ashwinchat.jobfinder.scraping.ScrapingStrategy;
import com.ashwinchat.jobfinder.selenium.SeleniumDriver;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public class TechinAsiaScrapingStrategy implements ScrapingStrategy {

    private static final String BASE_URL_FORMAT = "https://www.techinasia.com/jobs?job_category_name[]=Web%20Development&job_category_name[]=Enterprise%20Software%20&%20Systems&job_type_name[]=Full-time&location_name[]=Singapore&page=";
    private static final String AGENCY_NAME = "TechInAsia";
    private WebDriver webDriver = SeleniumDriver.getInstance().getDriver();

    private static final int NORMAL_SCENARIO = 0;
    private static final int NOT_YEARS_EXP_REQUIRED = 1;
    private static final int LESS_THAN_1_YEAR = 2;

    @Override
    public List<ScrapedInfo> scrape() {
        List<String> allLinks = this.findAllJobLinks();
        return allLinks.stream().map(this::getJobInfoFromAPage).collect(Collectors.toList());
    }

    private List<String> findAllJobLinks() {
        List<String> allLinks = new ArrayList<>();
        for (int i = 1;; ++i) {
            this.webDriver.get(BASE_URL_FORMAT + i);
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
        ScrapedInfo scrapedInfo = new ScrapedInfo();
        scrapedInfo.setAgency(AGENCY_NAME);
        scrapedInfo.setUrl(url);

        this.webDriver.get(url);

        String title = this.webDriver.findElement(By.className("entity__name")).getText();
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
        if (StringUtils.contains(salary, "Up to SGD")) {
            String[] stringSplit = this.splitParagraphTag(salary);
            scrapedInfo.setPayMin(BigDecimal.ZERO);
            scrapedInfo.setPayMax(new BigDecimal(stringSplit[0]));
        } else if (StringUtils.contains(salary, "Salary Range")) {
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
