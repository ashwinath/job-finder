package com.ashwinchat.jobfinder.filter.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import com.ashwinchat.jobfinder.constants.Constants;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public class MinExperienceFilter extends FilterTemplate {

    public MinExperienceFilter() {
        super(Constants.KEY_MIN_EXP);
    }

    @Override
    protected Predicate<ScrapedInfo> filterCriteria(List<String> valuesToFilter) {
        try {
            // we only take the first value
            String value = valuesToFilter.stream().findFirst().orElse("0");
            BigDecimal valueBd = new BigDecimal(value);

            return x -> {
                BigDecimal minExp = x.getExpMin();
                if (Objects.nonNull(minExp)) {
                    return minExp.compareTo(valueBd) <= 0;
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
