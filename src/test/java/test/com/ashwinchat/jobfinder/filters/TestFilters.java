package test.com.ashwinchat.jobfinder.filters;

import java.math.BigDecimal;
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
import com.ashwinchat.jobfinder.factory.FilterFactory;
import com.ashwinchat.jobfinder.filter.Filter;
import com.ashwinchat.jobfinder.filter.impl.CompanyNameFilter;
import com.ashwinchat.jobfinder.filter.impl.JobDescriptionFilter;
import com.ashwinchat.jobfinder.filter.impl.MaxExperienceFilter;
import com.ashwinchat.jobfinder.filter.impl.MaxSalaryFilter;
import com.ashwinchat.jobfinder.filter.impl.MinExperienceFilter;
import com.ashwinchat.jobfinder.filter.impl.MinSalaryFilter;
import com.ashwinchat.jobfinder.filter.impl.NegativeJobDescriptionFilter;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

import test.com.ashwinchat.jobfinder.fixtures.Fixtures;
import test.com.ashwinchat.jobfinder.setup.TestCase;

public class TestFilters extends TestCase {

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

        try (PreparedStatement statement = connection.prepareStatement(INSERT_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_MIN_EXP);
            statement.setString(3, "1");
            statement.execute();
        }

        try (PreparedStatement statement = connection.prepareStatement(INSERT_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_MAX_EXP);
            statement.setString(3, "4");
            statement.execute();
        }

        try (PreparedStatement statement = connection.prepareStatement(INSERT_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_JOB_DESCR_NEGATIVE);
            statement.setString(3, "SLAVE");
            statement.execute();
        }

        try (PreparedStatement statement = connection.prepareStatement(INSERT_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_MAX_PAY);
            statement.setString(3, "9000");
            statement.execute();
        }

        try (PreparedStatement statement = connection.prepareStatement(INSERT_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_MIN_PAY);
            statement.setString(3, "8000");
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

        try (PreparedStatement statement = connection.prepareStatement(DELETE_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_MIN_EXP);
            statement.setString(3, "1");
            statement.execute();
        }

        try (PreparedStatement statement = connection.prepareStatement(DELETE_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_MAX_EXP);
            statement.setString(3, "4");
            statement.execute();
        }

        try (PreparedStatement statement = connection.prepareStatement(DELETE_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_MAX_EXP);
            statement.setString(3, "error config");
            statement.execute();
        }

        try (PreparedStatement statement = connection.prepareStatement(DELETE_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_MIN_EXP);
            statement.setString(3, "error config");
            statement.execute();
        }

        try (PreparedStatement statement = connection.prepareStatement(DELETE_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_JOB_DESCR_NEGATIVE);
            statement.setString(3, "SLAVE");
            statement.execute();
        }

        try (PreparedStatement statement = connection.prepareStatement(DELETE_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_MAX_PAY);
            statement.setString(3, "9000");
            statement.execute();
        }

        try (PreparedStatement statement = connection.prepareStatement(DELETE_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_MIN_PAY);
            statement.setString(3, "8000");
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

        Filter jdFilter = new JobDescriptionFilter();
        List<ScrapedInfo> filteredInfos = jdFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isEmpty(filteredInfos));
    }

    @Test
    public void testJdFilterNotBlocking() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        infos.add(info);

        Filter jdFilter = new JobDescriptionFilter();
        List<ScrapedInfo> filteredInfos = jdFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isNotEmpty(filteredInfos));
    }

    @Test
    public void testNegativeJdFilterBlocking() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        info.setJobDescr("slave");
        infos.add(info);

        Filter jdFilter = new NegativeJobDescriptionFilter();
        List<ScrapedInfo> filteredInfos = jdFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isEmpty(filteredInfos));
    }

    @Test
    public void testNegativeJdFilterNotBlocking() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        infos.add(info);

        Filter jdFilter = new NegativeJobDescriptionFilter();
        List<ScrapedInfo> filteredInfos = jdFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isNotEmpty(filteredInfos));
    }

    @Test
    public void testExpMinFilterBlocking() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        info.setExpMin(new BigDecimal("2"));
        infos.add(info);

        Filter minExpFilter = new MinExperienceFilter();
        List<ScrapedInfo> filteredInfos = minExpFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isEmpty(filteredInfos));
    }

    @Test
    public void testExpMaxFilterBlocking() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        info.setExpMax(new BigDecimal("201"));
        infos.add(info);

        Filter maxExpFilter = new MaxExperienceFilter();
        List<ScrapedInfo> filteredInfos = maxExpFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isEmpty(filteredInfos));
    }

    @Test
    public void testExpMaxFilterError() throws Exception {
        this.cleanup();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_MAX_EXP);
            statement.setString(3, "error config");
            statement.execute();
        }

        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        infos.add(info);

        Filter maxExpFilter = new MaxExperienceFilter();
        List<ScrapedInfo> filteredInfos = maxExpFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isNotEmpty(filteredInfos));

    }

    @Test
    public void testExpMinFilterError() throws Exception {
        this.cleanup();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SYSTEM_CONFIG_STATEMENT)) {
            statement.setString(1, Constants.SYS_CD_FILTER);
            statement.setString(2, Constants.KEY_MIN_EXP);
            statement.setString(3, "error config");
            statement.execute();
        }

        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        infos.add(info);

        Filter minExpFilter = new MinExperienceFilter();
        List<ScrapedInfo> filteredInfos = minExpFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isNotEmpty(filteredInfos));

    }

    @Test
    public void testExpMinFilterNull() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        info.setExpMin(null);
        infos.add(info);

        Filter minExpFilter = new MinExperienceFilter();
        List<ScrapedInfo> filteredInfos = minExpFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isNotEmpty(filteredInfos));

    }

    @Test
    public void testExpMaxFilterNull() throws Exception {

        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        info.setExpMax(null);
        infos.add(info);

        Filter maxExpFilter = new MaxExperienceFilter();
        List<ScrapedInfo> filteredInfos = maxExpFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isNotEmpty(filteredInfos));

    }

    @Test
    public void testPayMinFilterNull() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        info.setPayMin(null);
        infos.add(info);

        Filter minExpFilter = new MinSalaryFilter();
        List<ScrapedInfo> filteredInfos = minExpFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isNotEmpty(filteredInfos));

    }

    @Test
    public void testPayMaxFilterNull() throws Exception {

        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        info.setPayMax(null);
        infos.add(info);

        Filter maxExpFilter = new MaxSalaryFilter();
        List<ScrapedInfo> filteredInfos = maxExpFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isNotEmpty(filteredInfos));

    }

    @Test
    public void testPayMinFilterBlocking() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        info.setPayMin(new BigDecimal("2"));
        infos.add(info);

        Filter minExpFilter = new MinSalaryFilter();
        List<ScrapedInfo> filteredInfos = minExpFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isEmpty(filteredInfos));
    }

    @Test
    public void testPayMaxFilterBlocking() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        info.setPayMax(new BigDecimal("8000"));
        infos.add(info);

        Filter maxExpFilter = new MaxSalaryFilter();
        List<ScrapedInfo> filteredInfos = maxExpFilter.filter(infos);

        Assert.assertTrue(CollectionUtils.isEmpty(filteredInfos));
    }

    @Test
    public void testCompositeFilterBlocking() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        infos.add(info);

        Filter compositeFilter = FilterFactory.getInstance().buildCompositeFilter();
        List<ScrapedInfo> filteredInfos = compositeFilter.filter(infos);
        Assert.assertTrue(CollectionUtils.isEmpty(filteredInfos));
    }

    @Test
    public void testCompositeFilterNotBlocking() throws Exception {
        List<ScrapedInfo> infos = new ArrayList<>();
        ScrapedInfo info = Fixtures.createScrapedInfo();
        info.setCompanyName("lalala");
        infos.add(info);

        Filter compositeFilter = FilterFactory.getInstance().buildCompositeFilter();
        List<ScrapedInfo> filteredInfos = compositeFilter.filter(infos);
        Assert.assertTrue(CollectionUtils.isNotEmpty(filteredInfos));
    }
}
