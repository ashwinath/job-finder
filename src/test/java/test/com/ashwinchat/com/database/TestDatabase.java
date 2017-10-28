package test.com.ashwinchat.com.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ashwinchat.jobfinder.config.SqLiteDatabaseConnection;
import com.ashwinchat.jobfinder.database.DatabaseUtil;
import com.ashwinchat.jobfinder.view.ScrapedInfo;
import com.ashwinchat.jobfinder.view.SystemConfig;

public class TestDatabase {
    private static final String COMPANY_NAME = "test company name";
    private static final String TEST_VALUE = "test value";
    private static final String TEST_KEY = "testkey";
    private static final String TEST_SYS_CD = "syscd";
    private Connection connection;

    @Before
    public void init() throws Exception {
        this.connection = SqLiteDatabaseConnection.getInstance().getSqliteConnection();

        try (PreparedStatement statement = connection
                .prepareStatement("delete from ScrapeInfo where companyName = ?")) {
            statement.setString(1, COMPANY_NAME);
            statement.execute();
        }

        try (PreparedStatement statement = connection
                .prepareStatement("delete from SystemConfig where sysCd = ? and key = ?")) {
            statement.setString(1, TEST_SYS_CD);
            statement.setString(2, TEST_KEY);
            statement.execute();
        }
    }

    @Test
    public void testInsertAllFields() throws Exception {
        ScrapedInfo scrapedInfo = this.createScrapedInfo();
        List<ScrapedInfo> scrapedInfos = new LinkedList<>();
        scrapedInfos.add(scrapedInfo);
        DatabaseUtil.insertScrapedInfoIntoDb(scrapedInfos);

        try (PreparedStatement statement = connection
                .prepareStatement("select * from ScrapeInfo where companyName = ?")) {
            statement.setString(1, COMPANY_NAME);
            ResultSet result = statement.executeQuery();
            Assert.assertTrue(result.next());
            Assert.assertEquals(COMPANY_NAME, result.getString(2));
        }
    }

    @Test
    public void testInsertOnlyMandatoryFields() throws Exception {
        ScrapedInfo scrapedInfo = this.createMinScrapedInfo();
        List<ScrapedInfo> scrapedInfos = new LinkedList<>();
        scrapedInfos.add(scrapedInfo);
        DatabaseUtil.insertScrapedInfoIntoDb(scrapedInfos);

        try (PreparedStatement statement = connection
                .prepareStatement("select * from ScrapeInfo where companyName = ?")) {
            statement.setString(1, COMPANY_NAME);
            ResultSet result = statement.executeQuery();
            Assert.assertTrue(result.next());
            Assert.assertEquals(COMPANY_NAME, result.getString(2));
        }
    }

    @Test
    public void testMultipleInserts() throws Exception {
        List<ScrapedInfo> scrapedInfos = new LinkedList<>();
        ScrapedInfo scrapedInfo = this.createScrapedInfo();
        ScrapedInfo scrapedInfo2 = this.createMinScrapedInfo();
        scrapedInfos.add(scrapedInfo);
        scrapedInfos.add(scrapedInfo2);
        DatabaseUtil.insertScrapedInfoIntoDb(scrapedInfos);

        try (PreparedStatement statement = connection
                .prepareStatement("select * from ScrapeInfo where companyName = ?")) {
            statement.setString(1, COMPANY_NAME);
            ResultSet result = statement.executeQuery();
            Assert.assertTrue(result.next());
            Assert.assertEquals(COMPANY_NAME, result.getString(2));
            Assert.assertTrue(result.next());
            Assert.assertEquals(COMPANY_NAME, result.getString(2));
        }

    }

    @Test
    public void testQuerySystemConfig() throws Exception {
        try (PreparedStatement statement = connection
                .prepareStatement("insert into SystemConfig (sysCd, key, value) values (?, ?, ?)")) {
            statement.setString(1, TEST_SYS_CD);
            statement.setString(2, TEST_KEY);
            statement.setString(3, TEST_VALUE);
            statement.execute();

            List<SystemConfig> systemConfigs = DatabaseUtil.getSystemConfigValue(TEST_SYS_CD, TEST_KEY);
            Assert.assertTrue(CollectionUtils.isNotEmpty(systemConfigs));
            SystemConfig systemConfig = systemConfigs.get(0);
            Assert.assertEquals(TEST_SYS_CD, systemConfig.getSysCd());
            Assert.assertEquals(TEST_KEY, systemConfig.getKey());
            Assert.assertEquals(TEST_VALUE, systemConfig.getValue());
        }

    }

    private ScrapedInfo createMinScrapedInfo() {
        ScrapedInfo scrapedInfo = this.createScrapedInfo();
        scrapedInfo.setPayMin(null);
        scrapedInfo.setPayMax(null);
        scrapedInfo.setExpMax(null);
        scrapedInfo.setExpMin(null);

        return scrapedInfo;
    }

    private ScrapedInfo createScrapedInfo() {
        ScrapedInfo scrapedInfo = new ScrapedInfo();
        scrapedInfo.setAgency("test agency");
        scrapedInfo.setCompanyName(COMPANY_NAME);
        scrapedInfo.setCreOn(LocalDateTime.now());
        scrapedInfo.setExpMax(BigDecimal.ONE);
        scrapedInfo.setExpMin(BigDecimal.ONE);
        scrapedInfo.setJobDescr("Test job descr");
        scrapedInfo.setPayMax(BigDecimal.ONE);
        scrapedInfo.setPayMin(BigDecimal.ONE);
        scrapedInfo.setTitle("test title");
        scrapedInfo.setUrl("testurl.com");
        return scrapedInfo;
    }
}
