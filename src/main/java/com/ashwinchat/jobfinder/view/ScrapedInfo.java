package com.ashwinchat.jobfinder.view;

import java.time.LocalDateTime;

public class ScrapedInfo {
    private String title;
    private String companyName;
    private String url;
    private String jobDescr;
    private String agency;
    private int payMin;
    private int payMax;
    private int expMin;
    private int expMax;
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

    public int getPayMin() {
        return this.payMin;
    }

    public void setPayMin(int payMin) {
        this.payMin = payMin;
    }

    public int getPayMax() {
        return this.payMax;
    }

    public void setPayMax(int payMax) {
        this.payMax = payMax;
    }

    public int getExpMin() {
        return this.expMin;
    }

    public void setExpMin(int expMin) {
        this.expMin = expMin;
    }

    public int getExpMax() {
        return this.expMax;
    }

    public void setExpMax(int expMax) {
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
