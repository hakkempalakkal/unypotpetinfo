package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by HP on 23-05-2016.
 */
public class CountryCodeBean implements Serializable {

    public CountryCodeBean() {
    }

    public CountryCodeBean(String countryName, String countryCode, int county_icon) {
        this.countryName = countryName;
        this.countryCode = countryCode;
        this.county_icon = county_icon;
    }

    String countryName, countryCode;
    int county_icon;

    public int getCounty_icon() {
        return county_icon;
    }

    public void setCounty_icon(int county_icon) {
        this.county_icon = county_icon;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
