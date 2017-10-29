package com.ashwinchat.jobfinder.filter.impl;

import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import com.ashwinchat.jobfinder.constants.Constants;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public class JobDescriptionFilter extends FilterTemplate {

    public JobDescriptionFilter() {
        super(Constants.KEY_JOB_DESCR);
    }

    @Override
    protected Predicate<ScrapedInfo> filterCriteria(List<String> valuesToFilter) {
        return x -> {
            String jobDescr = StringUtils.upperCase(x.getJobDescr());
            return valuesToFilter.stream().filter(filterValue -> StringUtils.contains(StringUtils.upperCase(jobDescr),
                    StringUtils.upperCase(filterValue))).findFirst().map(value -> true).orElse(false);
        };
    }

}
