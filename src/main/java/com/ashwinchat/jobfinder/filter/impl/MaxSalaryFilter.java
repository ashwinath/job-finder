package com.ashwinchat.jobfinder.filter.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import com.ashwinchat.jobfinder.constants.Constants;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public class MaxSalaryFilter extends FilterTemplate {

    public MaxSalaryFilter() {
        super(Constants.KEY_MAX_PAY);
    }

    @Override
    protected Predicate<ScrapedInfo> filterCriteria(List<String> valuesToFilter) {
        return x -> {
            String value = valuesToFilter.stream().findFirst().orElse("0");
            BigDecimal valueBd = new BigDecimal(value);
            BigDecimal maxPay = x.getPayMax();
            if (Objects.isNull(maxPay)) {
                // no filter applied
                return true;
            }
            return maxPay.compareTo(valueBd) >= 0;
        };
    }

}
