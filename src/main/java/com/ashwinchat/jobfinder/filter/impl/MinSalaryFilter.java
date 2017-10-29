package com.ashwinchat.jobfinder.filter.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import com.ashwinchat.jobfinder.constants.Constants;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public class MinSalaryFilter extends FilterTemplate {

    public MinSalaryFilter() {
        super(Constants.KEY_MIN_PAY);
    }

    @Override
    protected Predicate<ScrapedInfo> filterCriteria(List<String> valuesToFilter) {
        return x -> {
            String value = valuesToFilter.stream().findFirst().orElse("0");
            BigDecimal valueBd = new BigDecimal(value);
            BigDecimal minPay = x.getPayMin();
            if (Objects.isNull(minPay)) {
                // no filter applied
                return true;
            }
            return minPay.compareTo(valueBd) >= 0;
        };
    }

}
