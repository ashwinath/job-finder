package com.ashwinchat.jobfinder.view;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ScrapedInfo {
    private String title;
    private String companyName;
    private String url;
    private String jobDescr;
    private String agency;
    private BigDecimal payMin;
    private BigDecimal payMax;
    private BigDecimal expMin;
    private BigDecimal expMax;
    private LocalDateTime creOn;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getJobDescr() {
        return this.jobDescr;
    }

    public void setJobDescr(String jobDescr) {
        this.jobDescr = jobDescr;
    }

    public String getAgency() {
        return this.agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public BigDecimal getPayMin() {
        return this.payMin;
    }

    public void setPayMin(BigDecimal payMin) {
        this.payMin = payMin;
    }

    public BigDecimal getPayMax() {
        return this.payMax;
    }

    public void setPayMax(BigDecimal payMax) {
        this.payMax = payMax;
    }

    public BigDecimal getExpMin() {
        return this.expMin;
    }

    public void setExpMin(BigDecimal expMin) {
        this.expMin = expMin;
    }

    public BigDecimal getExpMax() {
        return this.expMax;
    }

    public void setExpMax(BigDecimal expMax) {
        this.expMax = expMax;
    }

    public LocalDateTime getCreOn() {
        return this.creOn;
    }

    public void setCreOn(LocalDateTime creOn) {
        this.creOn = creOn;
    }

    @Override
    public String toString() {
        return String.format(
                "title: %s, companyName: %s, url: %s, jobDescr: %s, agency: %s, payMin: %s, payMax: %s, expMin: %s, expMax: %s, creOn: %s",
                this.title, this.companyName, this.url, this.jobDescr, this.agency, this.payMin, this.payMax,
                this.expMin, this.expMax, this.creOn);
    }

}
