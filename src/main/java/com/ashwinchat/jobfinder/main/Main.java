package com.ashwinchat.jobfinder.main;

import java.util.logging.Logger;

import com.ashwinchat.jobfinder.config.LogFormatter;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String... args) throws Exception {
        LogFormatter.applyFormat(LOGGER);
        LOGGER.info("Scraper has started.");
        // ScrapingStrategy strat = new LogStrategyDecorator(new
        // TechinAsiaScrapingStrategy(), "TechInAsia");
        // List<ScrapedInfo> scrapedInfos = strat.scrape();
    }

}
