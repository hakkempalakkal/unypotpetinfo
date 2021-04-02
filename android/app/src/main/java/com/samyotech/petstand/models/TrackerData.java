package com.samyotech.petstand.models;

/**
 * Created by welcome pc on 07-01-2018.
 */

public class TrackerData
{
    String id;
    String deviceid;
    String datetime;
    String steps;
    String totalsteps;

    public TrackerData(String id, String deviceid, String datetime, String steps, String totalsteps) {
        this.id = id;
        this.deviceid = deviceid;
        this.datetime = datetime;
        this.steps = steps;
        this.totalsteps = totalsteps;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getTotalsteps() {
        return totalsteps;
    }

    public void setTotalsteps(String totalsteps) {
        this.totalsteps = totalsteps;
    }
}
