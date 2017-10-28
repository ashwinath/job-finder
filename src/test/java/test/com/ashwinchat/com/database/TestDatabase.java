package test.com.ashwinchat.com.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.ashwinchat.jobfinder.config.SqLiteDatabaseConnection;
import com.ashwinchat.jobfinder.database.DatabaseUtil;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public class TestDatabase {
    private static final String COMPANY_NAME = "test company name";
    private Connection connection;

    @Before
    public void init() throws Exception {
        this.connection = SqLiteDatabaseConnection.getInstance().getSqliteConnection();

        try (PreparedStatement statement = connection
                .prepareStatement("delete from ScrapeInfo where companyName = ?")) {
            statement.setString(1, COMPANY_NAME);
            statement.execute();
        }
    }

    @Test
    public void testInsertAllFields() throws Exception {
        ScrapedInfo scrapedInfo = this.createScrapedInfo();
        DatabaseUtil.insertScrapedInfoIntoDb(scrapedInfo);

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
        ScrapedInfo scrapedInfo = this.createScrapedInfo();
        scrapedInfo.setPayMin(null);
        scrapedInfo.setPayMax(null);
        scrapedInfo.setExpMax(null);
        scrapedInfo.setExpMin(null);
        DatabaseUtil.insertScrapedInfoIntoDb(scrapedInfo);

        try (PreparedStatement statement = connection
                .prepareStatement("select * from ScrapeInfo where companyName = ?")) {
            statement.setString(1, COMPANY_NAME);
            ResultSet result = statement.executeQuery();
            Assert.assertTrue(result.next());
            Assert.assertEquals(COMPANY_NAME, result.getString(2));
        }
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
