package com.ashwinchat.jobfinder.main;

import java.util.List;
import java.util.logging.Logger;

import com.ashwinchat.jobfinder.config.LogFormatter;
import com.ashwinchat.jobfinder.decorator.LogStrategyDecorator;
import com.ashwinchat.jobfinder.scraping.ScrapingStrategy;
import com.ashwinchat.jobfinder.scraping.impl.TechinAsiaScrapingStrategy;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String... args) {
        LogFormatter.applyFormat(LOGGER);
        LOGGER.info("Scraper has started.");
        ScrapingStrategy strat = new LogStrategyDecorator(new TechinAsiaScrapingStrategy(), "TechInAsia");
        List<ScrapedInfo> scrapedInfos = strat.scrape();
    }

}
