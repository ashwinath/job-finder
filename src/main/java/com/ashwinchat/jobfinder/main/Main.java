package com.ashwinchat.jobfinder.main;

import java.util.List;

import com.ashwinchat.jobfinder.scraping.ScrapingStrategy;
import com.ashwinchat.jobfinder.scraping.impl.TechinAsiaScrapingStrategy;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public class Main {
    public static void main(String... args) {
        ScrapingStrategy strat = new TechinAsiaScrapingStrategy();
        List<ScrapedInfo> scrapedInfos = strat.scrape();
        scrapedInfos.forEach(System.out::println);
        System.out.println(scrapedInfos.size());
    }
}
