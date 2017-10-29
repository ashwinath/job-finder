package com.ashwinchat.jobfinder.main;

import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.ashwinchat.jobfinder.config.LogFormatter;
import com.ashwinchat.jobfinder.constants.Constants;
import com.ashwinchat.jobfinder.factory.ScrapeMediatorFactory;
import com.ashwinchat.jobfinder.mediator.ScrapeMediator;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    public static void main(String... args) {
        System.setProperty(Constants.DATABASE_LOCATION_PROPERTY, "./database/sqlite.db");
        LogFormatter.applyFormat(LOGGER);
        LOGGER.info("Scraper has started.");

        ScrapeMediatorFactory scrapeMediatorFactory = ScrapeMediatorFactory.getInstance();
        try {
            ScrapeMediator techInAsiaMediator = scrapeMediatorFactory.createTechInAsiaScrapeMediator();
            techInAsiaMediator.process();
        } catch (Exception e) {
            LOGGER.info(ExceptionUtils.getStackTrace(e));
        }
    }

}
