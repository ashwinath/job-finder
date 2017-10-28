package com.ashwinchat.jobfinder.filter;

import java.util.List;

import com.ashwinchat.jobfinder.view.ScrapedInfo;

public interface Filter {
    List<ScrapedInfo> filter(List<ScrapedInfo> scrapedInfos);
}
