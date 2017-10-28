package com.ashwinchat.jobfinder.filter.impl;

import java.util.Arrays;
import java.util.List;

import com.ashwinchat.jobfinder.filter.Filter;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public class CompositeFilter implements Filter {

    private List<Filter> filters;

    public CompositeFilter(Filter... filters) {
        this.filters = Arrays.asList(filters);
    }

    @Override
    public List<ScrapedInfo> filter(List<ScrapedInfo> scrapedInfos) {
        List<ScrapedInfo> scrapedInfoFiltered = scrapedInfos;
        for (Filter filter : filters) {
            scrapedInfoFiltered = filter.filter(scrapedInfoFiltered);
        }
        return scrapedInfoFiltered;
    }

}
