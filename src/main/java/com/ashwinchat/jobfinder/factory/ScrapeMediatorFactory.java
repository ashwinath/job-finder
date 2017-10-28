package com.ashwinchat.jobfinder.factory;

import java.util.Objects;

import com.ashwinchat.jobfinder.constants.Constants;
import com.ashwinchat.jobfinder.decorator.LogScrapeMediatorDecorator;
import com.ashwinchat.jobfinder.decorator.LogStrategyDecorator;
import com.ashwinchat.jobfinder.mediator.ScrapeMediator;
import com.ashwinchat.jobfinder.mediator.impl.ScrapeMediatorImpl;
import com.ashwinchat.jobfinder.scraping.ScrapingStrategy;
import com.ashwinchat.jobfinder.scraping.impl.TechinAsiaScrapingStrategy;

public class ScrapeMediatorFactory {
    private static ScrapeMediatorFactory instance = null;

    private ScrapeMediatorFactory() {
    }

    public static ScrapeMediatorFactory getInstance() {
        if (Objects.isNull(instance)) {
            instance = new ScrapeMediatorFactory();
        }
        return instance;
    }

    public ScrapeMediator createTechInAsiaScrapeMediator() {
        ScrapingStrategy techInAsiaScrapingStrategy = new LogStrategyDecorator(new TechinAsiaScrapingStrategy(),
                Constants.TECH_IN_ASIA_NAME);

        return new LogScrapeMediatorDecorator(new ScrapeMediatorImpl(techInAsiaScrapingStrategy),
                Constants.TECH_IN_ASIA_NAME);
    }

}
