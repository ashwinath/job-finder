package test.com.ashwinchat.jobfinder.setup;

import com.ashwinchat.jobfinder.constants.Constants;

public class TestCase {

    static {
        System.setProperty(Constants.DATABASE_LOCATION_PROPERTY, "./database/sqliteTest.db");
    }
}
