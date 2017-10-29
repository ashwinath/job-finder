package com.ashwinchat.jobfinder.factory;

import java.util.Objects;

import com.ashwinchat.jobfinder.filter.Filter;
import com.ashwinchat.jobfinder.filter.impl.CompanyNameFilter;
import com.ashwinchat.jobfinder.filter.impl.CompositeFilter;
import com.ashwinchat.jobfinder.filter.impl.JobDescriptionFilter;
import com.ashwinchat.jobfinder.filter.impl.MaxExperienceFilter;
import com.ashwinchat.jobfinder.filter.impl.MinExperienceFilter;

public class FilterFactory {
    private static FilterFactory instance = null;

    private FilterFactory() {
    }

    public static FilterFactory getInstance() {
        if (Objects.isNull(instance)) {
            instance = new FilterFactory();
        }
        return instance;
    }

    public Filter buildCompositeFilter() {
        Filter jobDescrFilter = new JobDescriptionFilter();
        Filter companyNameFilter = new CompanyNameFilter();

        // Experience Filters
        Filter minExpFilter = new MinExperienceFilter();
        Filter maxExpFilter = new MaxExperienceFilter();
        Filter experienceFilter = new CompositeFilter(maxExpFilter, minExpFilter);

        return new CompositeFilter(jobDescrFilter, companyNameFilter, experienceFilter);
    }

}
