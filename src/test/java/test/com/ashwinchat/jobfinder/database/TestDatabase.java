package test.com.ashwinchat.jobfinder.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

import test.com.ashwinchat.jobfinder.fixtures.Fixtures;

public class TestDatabase {
    private Connection connection;

    @Before
    public void init() throws Exception {
        this.connection = SqLiteDatabaseConnection.getInstance().getSqliteConnection();

        try (PreparedStatement statement = connection
                .prepareStatement("delete from ScrapeInfo where companyName = ?")) {
            statement.setString(1, Fixtures.COMPANY_NAME);
            statement.execute();
        }

        try (PreparedStatement statement = connection
                .prepareStatement("delete from SystemConfig where sysCd = ? and key = ?")) {
            statement.setString(1, Fixtures.TEST_SYS_CD);
            statement.setString(2, Fixtures.TEST_KEY);
            statement.execute();
        }
    }

    @Test
    public void testInsertAllFields() throws Exception {
        ScrapedInfo scrapedInfo = Fixtures.createScrapedInfo();
        List<ScrapedInfo> scrapedInfos = new LinkedList<>();
        scrapedInfos.add(scrapedInfo);
        DatabaseUtil.insertScrapedInfoIntoDb(scrapedInfos);

        try (PreparedStatement statement = connection
                .prepareStatement("select * from ScrapeInfo where companyName = ?")) {
            statement.setString(1, Fixtures.COMPANY_NAME);
            ResultSet result = statement.executeQuery();
            Assert.assertTrue(result.next());
            Assert.assertEquals(Fixtures.COMPANY_NAME, result.getString(2));
        }
    }

    @Test
    public void testInsertOnlyMandatoryFields() throws Exception {
        ScrapedInfo scrapedInfo = Fixtures.createMinScrapedInfo();
        List<ScrapedInfo> scrapedInfos = new LinkedList<>();
        scrapedInfos.add(scrapedInfo);
        DatabaseUtil.insertScrapedInfoIntoDb(scrapedInfos);

        try (PreparedStatement statement = connection
                .prepareStatement("select * from ScrapeInfo where companyName = ?")) {
            statement.setString(1, Fixtures.COMPANY_NAME);
            ResultSet result = statement.executeQuery();
            Assert.assertTrue(result.next());
            Assert.assertEquals(Fixtures.COMPANY_NAME, result.getString(2));
        }
    }

    @Test
    public void testMultipleInserts() throws Exception {
        List<ScrapedInfo> scrapedInfos = new LinkedList<>();
        ScrapedInfo scrapedInfo = Fixtures.createScrapedInfo();
        ScrapedInfo scrapedInfo2 = Fixtures.createMinScrapedInfo();
        scrapedInfos.add(scrapedInfo);
        scrapedInfos.add(scrapedInfo2);
        DatabaseUtil.insertScrapedInfoIntoDb(scrapedInfos);

        try (PreparedStatement statement = connection
                .prepareStatement("select * from ScrapeInfo where companyName = ?")) {
            statement.setString(1, Fixtures.COMPANY_NAME);
            ResultSet result = statement.executeQuery();
            Assert.assertTrue(result.next());
            Assert.assertEquals(Fixtures.COMPANY_NAME, result.getString(2));
            Assert.assertTrue(result.next());
            Assert.assertEquals(Fixtures.COMPANY_NAME, result.getString(2));
        }

    }

    @Test
    public void testQuerySystemConfig() throws Exception {
        try (PreparedStatement statement = connection
                .prepareStatement("insert into SystemConfig (sysCd, key, value) values (?, ?, ?)")) {
            statement.setString(1, Fixtures.TEST_SYS_CD);
            statement.setString(2, Fixtures.TEST_KEY);
            statement.setString(3, Fixtures.TEST_VALUE);
            statement.execute();

            List<SystemConfig> systemConfigs = DatabaseUtil.getSystemConfigValue(Fixtures.TEST_SYS_CD,
                    Fixtures.TEST_KEY);
            Assert.assertTrue(CollectionUtils.isNotEmpty(systemConfigs));
            SystemConfig systemConfig = systemConfigs.get(0);
            Assert.assertEquals(Fixtures.TEST_SYS_CD, systemConfig.getSysCd());
            Assert.assertEquals(Fixtures.TEST_KEY, systemConfig.getKey());
            Assert.assertEquals(Fixtures.TEST_VALUE, systemConfig.getValue());
        }

    }

}
