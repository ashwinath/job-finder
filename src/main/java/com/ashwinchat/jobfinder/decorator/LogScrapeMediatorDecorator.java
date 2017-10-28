package com.ashwinchat.jobfinder.decorator;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.ashwinchat.jobfinder.config.LogFormatter;
import com.ashwinchat.jobfinder.mediator.ScrapeMediator;

public class LogScrapeMediatorDecorator implements ScrapeMediator {

    private ScrapeMediator scrapeMediator;
    private String name;
    private Logger logger;

    public LogScrapeMediatorDecorator(ScrapeMediator scrapeMediator, String name) {
        this.scrapeMediator = scrapeMediator;
        this.name = name;
        this.logger = Logger.getLogger(name);
        LogFormatter.applyFormat(this.logger);
    }

    @Override
    public void process() {
        long startTime = System.currentTimeMillis();
        if (this.logger.isLoggable(Level.INFO)) {
            this.logger.info(String.format("Beginning to process: %s", this.name));
        }

        this.scrapeMediator.process();
        long endTime = System.currentTimeMillis();

        if (this.logger.isLoggable(Level.INFO)) {
            this.logger
                    .info(String.format("Finished processing: %s. Took %ss", this.name, (endTime - startTime) / 1000));
        }
    }

}
