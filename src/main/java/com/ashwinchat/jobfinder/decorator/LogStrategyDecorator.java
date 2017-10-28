package com.ashwinchat.jobfinder.decorator;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ashwinchat.jobfinder.config.LogFormatter;
import com.ashwinchat.jobfinder.scraping.ScrapingStrategy;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public class LogStrategyDecorator implements ScrapingStrategy {
    private final ScrapingStrategy scrapingStrategy;
    private final String name;
    private final Logger logger;

    public LogStrategyDecorator(ScrapingStrategy scrapingStrategy, String name) {
        this.scrapingStrategy = scrapingStrategy;
        this.name = name;
        this.logger = Logger.getLogger(scrapingStrategy.getClass().getName());
        LogFormatter.applyFormat(this.logger);
    }

    @Override
    public List<ScrapedInfo> scrape() {
        long startTime = System.currentTimeMillis();
        if (this.logger.isLoggable(Level.INFO)) {
            this.logger.info(String.format("Begining to scrape: %s", this.name));
        }

        List<ScrapedInfo> scrapedInfo = this.scrapingStrategy.scrape();
        long endTime = System.currentTimeMillis();

        if (this.logger.isLoggable(Level.INFO)) {
            this.logger.info(String.format("Finished scraping: %s. Took %ss", this.name, (endTime - startTime) / 1000));
        }
        return scrapedInfo;
    }

}
