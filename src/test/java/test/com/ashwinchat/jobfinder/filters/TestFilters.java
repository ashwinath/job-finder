package test.com.ashwinchat.jobfinder.filters;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ashwinchat.jobfinder.config.SqLiteDatabaseConnection;
import com.ashwinchat.jobfinder.constants.Constants;
import com.ashwinchat.jobfinder.filter.Filter;
import com.ashwinchat.jobfinder.filter.impl.CompanyNameFilter;
import com.ashwinchat.jobfinder.filter.impl.CompositeFilter;
import com.ashwinchat.jobfinder.filter.impl.JobDescriptionFilter;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

import test.com.ashwinchat.jobfinder.fixtures.Fixtures;

public class TestFilters {

    private Connection connection;
    private static final String INSERT_SYSTEM_CONFIG_STATEMENT = "insert into SystemConfig (sysCd, key, value) values (?, ?, ?)";
    private static final String DELETE_SYSTEM_CONFIG_STATEMENT = "delete from SystemConfig where sysCd = ? and key = ? and value = ?";

    @Before
    public void init() throws Exception {
        this.connection = SqLiteDatabaseConnection.getInstance().getSqliteConnection();

        try (PreparedStatement statement = connection.prepareStatement(INSERT_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_COMPANY);
            statement.setString(3, Fixtures.COMPANY_NAME);
            statement.execute();
        }

        try (PreparedStatement statement = connection.prepareStatement(INSERT_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_JOB_DESCR);
            statement.setString(3, "test");
            statement.execute();
        }
    }

    @After
    public void cleanup() throws Exception {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_COMPANY);
            statement.setString(3, Fixtures.COMPANY_NAME);
            statement.execute();
        }

        try (PreparedStatement statement = connection.prepareStatement(DELETE_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_JOB_DESCR);
            statement.setString(3, "test");
            statement.execute();
        }

    }

    @Test
    public void testCompanyFilterBlocking() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        infos.add(info);

        Filter companyFilter = new CompanyNameFilter();
        List<ScrapedInfo> filteredInfos = companyFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isEmpty(filteredInfos));
    }

    @Test
    public void testCompanyFilterNotBlocking() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        info.setCompanyName("lalala");
        infos.add(info);

        Filter companyFilter = new CompanyNameFilter();
        List<ScrapedInfo> filteredInfos = companyFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isNotEmpty(filteredInfos));
    }

    @Test
    public void testJdFilterBlocking() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        info.setJobDescr("lalala");
        infos.add(info);

        Filter companyFilter = new JobDescriptionFilter();
        List<ScrapedInfo> filteredInfos = companyFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isEmpty(filteredInfos));
    }

    @Test
    public void testJdFilterNotBlocking() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        infos.add(info);

        Filter companyFilter = new JobDescriptionFilter();
        List<ScrapedInfo> filteredInfos = companyFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isNotEmpty(filteredInfos));
    }

    @Test
    public void testCompositeFilterBlocking() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        infos.add(info);

        Filter compositeFilter = new CompositeFilter(new JobDescriptionFilter(), new CompanyNameFilter());
        List<ScrapedInfo> filteredInfos = compositeFilter.filter(infos);
        Assert.assertTrue(CollectionUtils.isEmpty(filteredInfos));
    }

    @Test
    public void testCompositeFilterNotBlocking() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        info.setCompanyName("lalala");
        infos.add(info);

        Filter compositeFilter = new CompositeFilter(new JobDescriptionFilter(), new CompanyNameFilter());
        List<ScrapedInfo> filteredInfos = compositeFilter.filter(infos);
        Assert.assertTrue(CollectionUtils.isNotEmpty(filteredInfos));
    }
}
