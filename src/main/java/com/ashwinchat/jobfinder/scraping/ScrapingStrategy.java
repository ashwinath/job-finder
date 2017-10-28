package com.ashwinchat.jobfinder.scraping;

import com.ashwinchat.jobfinder.view.ScrapedInfo;

public interface ScrapingStrategy {
    ScrapedInfo scrape();
}
