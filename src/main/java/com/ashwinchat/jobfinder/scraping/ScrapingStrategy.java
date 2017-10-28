package com.ashwinchat.jobfinder.scraping;

import java.util.List;

import com.ashwinchat.jobfinder.view.ScrapedInfo;

public interface ScrapingStrategy {
    List<ScrapedInfo> scrape();
}
