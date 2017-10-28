package com.ashwinchat.jobfinder.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Objects;

import com.ashwinchat.jobfinder.config.SqLiteDatabaseConnection;
import com.ashwinchat.jobfinder.view.ScrapedInfo;

public final class DatabaseUtil {
    private static final String INSERT_STATEMENT_SCRAPED_INFO = "insert into ScrapeInfo (companyName, url, jobDescr, payMin, payMax, expMin, expMax, creOn) values (?, ?, ?, ?, ?, ?, ?, ?)";

    private DatabaseUtil() {
    }

    public static void insertScrapedInfoIntoDb(ScrapedInfo scrapedInfo) throws ClassNotFoundException, SQLException {
        Connection connection = SqLiteDatabaseConnection.getInstance().getSqliteConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_STATEMENT_SCRAPED_INFO)) {
            statement.setString(1, scrapedInfo.getCompanyName());
            statement.setString(2, scrapedInfo.getUrl());
            statement.setString(3, scrapedInfo.getJobDescr());

            BigDecimal payMin = scrapedInfo.getPayMin();
            if (Objects.nonNull(payMin)) {
                statement.setInt(4, payMin.intValueExact());
            }

            BigDecimal payMax = scrapedInfo.getPayMax();
            if (Objects.nonNull(payMax)) {
                statement.setInt(5, payMax.intValueExact());
            }

            BigDecimal expMin = scrapedInfo.getExpMin();
            if (Objects.nonNull(expMin)) {
                statement.setInt(6, expMin.intValueExact());
            }

            BigDecimal expMax = scrapedInfo.getExpMax();
            if (Objects.nonNull(expMax)) {
                statement.setInt(7, expMax.intValueExact());
            }

            statement.setLong(8, scrapedInfo.getCreOn().atZone(ZoneId.systemDefault()).toEpochSecond());
            statement.execute();

        }

    }
}
