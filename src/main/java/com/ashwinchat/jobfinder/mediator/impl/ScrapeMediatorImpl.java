package com.ashwinchat.jobfinder.mediator.impl;

import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.ashwinchat.jobfinder.database.DatabaseUtil;
import com.ashwinchat.jobfinder.mediator.ScrapeMediator;
import com.ashwinchat.jobfinder.scraping.ScrapingStrategy;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public class ScrapeMediatorImpl implements ScrapeMediator {

    private final ScrapingStrategy scrapingStrategy;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public ScrapeMediatorImpl(ScrapingStrategy scrapingStrategy) {
        this.scrapingStrategy = scrapingStrategy;
    }

    @Override
    public void process() {
        try {
            List<ScrapedInfo> scrapedInfos = this.scrapingStrategy.scrape();
            // potential filter method here
            DatabaseUtil.insertScrapedInfoIntoDb(scrapedInfos);
        } catch (Exception e) {
            this.logger.severe(ExceptionUtils.getStackTrace(e));
        }
    }

}
