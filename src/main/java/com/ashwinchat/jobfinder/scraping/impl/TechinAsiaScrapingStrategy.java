package com.ashwinchat.jobfinder.scraping.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.ashwinchat.jobfinder.scraping.ScrapingStrategy;
import com.ashwinchat.jobfinder.selenium.SeleniumDriver;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public class TechinAsiaScrapingStrategy implements ScrapingStrategy {

    private String url = "https://www.techinasia.com/jobs?job_category_name[]=Web%20Development&job_category_name[]=Enterprise%20Software%20&%20Systems&job_type_name[]=Full-time&location_name[]=Singapore&page=1";
    private WebDriver webDriver = SeleniumDriver.getInstance().getDriver();

    @Override
    public ScrapedInfo scrape() {
        List<String> allLinks = this.findAllJobLinks();
        allLinks.forEach(System.out::println);
        return null;
    }

    private List<String> findAllJobLinks() {
        webDriver.get(url);
        List<WebElement> searchResultWebElement = webDriver.findElements(By.className("search-results__item"));
        return searchResultWebElement.stream()
                .map(element -> element.findElement(By.className("job-listing")).getAttribute("href"))
                .collect(Collectors.toList());
    }

}
