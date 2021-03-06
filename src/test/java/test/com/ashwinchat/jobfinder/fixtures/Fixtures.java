package test.com.ashwinchat.jobfinder.fixtures;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.ashwinchat.jobfinder.view.ScrapedInfo;

public final class Fixtures {

    public static final String COMPANY_NAME = "TEST COMPANY NAME";
    public static final String TEST_VALUE = "TEST VALUE";
    public static final String TEST_KEY = "TESTKEY";
    public static final String TEST_SYS_CD = "SYSCD";

    private Fixtures() {
    }

    public static ScrapedInfo createMinScrapedInfo() {
        ScrapedInfo scrapedInfo = createScrapedInfo();
        scrapedInfo.setPayMin(null);
        scrapedInfo.setPayMax(null);
        scrapedInfo.setExpMax(null);
        scrapedInfo.setExpMin(null);

        return scrapedInfo;
    }

    public static ScrapedInfo createScrapedInfo() {
        ScrapedInfo scrapedInfo = new ScrapedInfo();
        scrapedInfo.setAgency("test agency");
        scrapedInfo.setCompanyName(COMPANY_NAME);
        scrapedInfo.setCreOn(LocalDateTime.now());
        scrapedInfo.setExpMax(new BigDecimal("4"));
        scrapedInfo.setExpMin(new BigDecimal("0"));
        scrapedInfo.setJobDescr("Test job descr");
        scrapedInfo.setPayMax(new BigDecimal("9000"));
        scrapedInfo.setPayMin(new BigDecimal("8000"));
        scrapedInfo.setTitle("test title");
        scrapedInfo.setUrl("testurl.com");
        return scrapedInfo;
    }
}
