package com.ashwinchat.jobfinder.filter.impl;

import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.StringUtils;

import com.ashwinchat.jobfinder.constants.Constants;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public class CompanyNameFilter extends FilterTemplate {

    public CompanyNameFilter() {
        super(Constants.KEY_COMPANY);
    }

    @Override
    protected Predicate<ScrapedInfo> filterCriteria(List<String> valuesToFilter) {
        return x -> {
            String companyName = StringUtils.upperCase(x.getCompanyName());
            boolean foundSimilar = valuesToFilter.stream()
                    .filter(filterValue -> StringUtils.equals(companyName, filterValue)).findFirst().map(value -> true)
                    .orElse(false);

            return !foundSimilar;
        };
    }

}
