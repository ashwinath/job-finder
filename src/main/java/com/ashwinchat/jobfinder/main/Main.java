package com.ashwinchat.jobfinder.main;

import java.util.logging.Logger;

import com.ashwinchat.jobfinder.config.LogFormatter;
import com.ashwinchat.jobfinder.factory.ScrapeMediatorFactory;
import com.ashwinchat.jobfinder.mediator.ScrapeMediator;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String... args) throws Exception {
        LogFormatter.applyFormat(LOGGER);
        LOGGER.info("Scraper has started.");

        ScrapeMediatorFactory scrapeMediatorFactory = ScrapeMediatorFactory.getInstance();
        ScrapeMediator techInAsiaMediator = scrapeMediatorFactory.createTechInAsiaScrapeMediator();
        techInAsiaMediator.process();
    }

}
