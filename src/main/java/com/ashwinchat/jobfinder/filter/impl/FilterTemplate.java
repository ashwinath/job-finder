package com.ashwinchat.jobfinder.filter.impl;

import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.ashwinchat.jobfinder.constants.Constants;
import com.ashwinchat.jobfinder.database.DatabaseUtil;
import com.ashwinchat.jobfinder.filter.Filter;
import com.ashwinchat.jobfinder.view.ScrapedInfo;
import com.ashwinchat.jobfinder.view.SystemConfig;

public abstract class FilterTemplate implements Filter {

    private Logger logger = Logger.getLogger(this.getClass().getName());
    private String key;

    public FilterTemplate(String key) {
        this.key = key;
    }

    @Override
    public List<ScrapedInfo> filter(List<ScrapedInfo> scrapedInfos) {
        try {
            List<SystemConfig> systemConfigs = DatabaseUtil.getSystemConfigValue(Constants.SYS_CD_FILTER, this.key);

            List<String> valuesToFilter = systemConfigs.stream().map(SystemConfig::getValue).map(StringUtils::upperCase)
                    .collect(Collectors.toList());

            return scrapedInfos.stream().filter(this.filterCriteria(valuesToFilter)).collect(Collectors.toList());
        } catch (Exception e) {
            logger.warning(String.format(Constants.CONFIG_NOT_FOUND, Constants.SYS_CD_FILTER, Constants.KEY_COMPANY,
                    ExceptionUtils.getStackTrace(e)));
        }

        // Do not filter if there's an error
        return scrapedInfos;
    }

    protected abstract Predicate<ScrapedInfo> filterCriteria(List<String> valuesToFilter);

}
