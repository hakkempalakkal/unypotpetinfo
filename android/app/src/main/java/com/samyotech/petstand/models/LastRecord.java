package com.samyotech.petstand.models;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by welcome pc on 08-02-2017.
 */
public class LastRecord {

    public List<LastRecordRow> weightupdate = new ArrayList<>();
    public Year year;

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }

    public List<LastRecordRow> getWeightupdate() {
        return weightupdate;
    }

    public void setWeightupdate(List<LastRecordRow> weightupdate) {
        this.weightupdate = weightupdate;
    }
}
