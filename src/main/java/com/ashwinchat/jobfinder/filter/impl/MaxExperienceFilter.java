package com.ashwinchat.jobfinder.filter.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import com.ashwinchat.jobfinder.constants.Constants;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public class MaxExperienceFilter extends FilterTemplate {

    public MaxExperienceFilter() {
        super(Constants.KEY_MAX_EXP);
    }

    @Override
    protected Predicate<ScrapedInfo> filterCriteria(List<String> valuesToFilter) {
        try {
            // we only take the first value
            String value = valuesToFilter.stream().findFirst().orElse("0");
            BigDecimal valueBd = new BigDecimal(value);

            return x -> {
                BigDecimal maxExp = x.getExpMax();
                if (Objects.nonNull(maxExp)) {
                    return maxExp.compareTo(valueBd) <= 0;
                }
                // No filtering if not specified
                return true;
            };
        } catch (NumberFormatException e) {
            // no filtering if there's an error
            return x -> true;
        }
    }

}
