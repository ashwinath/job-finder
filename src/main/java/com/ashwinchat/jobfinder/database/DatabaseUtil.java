package com.ashwinchat.jobfinder.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.ashwinchat.jobfinder.config.SqLiteDatabaseConnection;
import com.ashwinchat.jobfinder.view.ScrapedInfo;
import com.ashwinchat.jobfinder.view.SystemConfig;

public final class DatabaseUtil {
    private static final String INSERT_STATEMENT_SCRAPED_INFO = "insert into ScrapeInfo (companyName, url, jobDescr, payMin, payMax, expMin, expMax, creOn) values (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_STATEMENT_SYSTEM_CONFIG = "select sysCd, key, value from SystemConfig where sysCd = ? and key = ?";
    private static final Logger logger = Logger.getLogger(DatabaseUtil.class.getName());

    private DatabaseUtil() {
    }

    public static void insertScrapedInfoIntoDb(List<ScrapedInfo> scrapedInfos)
            throws ClassNotFoundException, SQLException {
        Connection connection = SqLiteDatabaseConnection.getInstance().getSqliteConnection();
        connection.setAutoCommit(false);
        try {
            for (ScrapedInfo scrapedInfo : scrapedInfos) {
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
                } catch (Exception e) {
                    logger.severe("Object with error" + scrapedInfo);
                    logger.severe(ExceptionUtils.getStackTrace(e));
                }
            }
        } finally {
            connection.commit();
        }
    }

    public static List<SystemConfig> getSystemConfigValue(String sysCd, String key)
            throws ClassNotFoundException, SQLException {
        List<SystemConfig> systemConfigs = new LinkedList<>();

        Connection connection = SqLiteDatabaseConnection.getInstance().getSqliteConnection();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_STATEMENT_SYSTEM_CONFIG)) {
            statement.setString(1, sysCd);
            statement.setString(2, key);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                SystemConfig systemConfig = new SystemConfig();

                String sysCdResult = resultSet.getString(1);
                systemConfig.setSysCd(sysCdResult);

                String keyResult = resultSet.getString(2);
                systemConfig.setKey(keyResult);

                String valueResult = resultSet.getString(3);
                systemConfig.setValue(valueResult);

                systemConfigs.add(systemConfig);
            }
        }

        return systemConfigs;
    }
}
