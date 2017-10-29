package com.ashwinchat.jobfinder.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

import com.ashwinchat.jobfinder.constants.Constants;

public class SqLiteDatabaseConnection {
    private static SqLiteDatabaseConnection instance;
    private Connection sqliteConnection;

    private SqLiteDatabaseConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        this.sqliteConnection = DriverManager
                .getConnection("jdbc:sqlite:" + System.getProperty(Constants.DATABASE_LOCATION_PROPERTY));
    }

    public static SqLiteDatabaseConnection getInstance() throws ClassNotFoundException, SQLException {
        if (Objects.isNull(instance)) {
            instance = new SqLiteDatabaseConnection();
        }
        return instance;
    }

    public Connection getSqliteConnection() {
        return this.sqliteConnection;
    }

}
