package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by welcome pc on 31-01-2017.
 */
public class ManualActivity implements Serializable
{
    String userEmail;
    String petId;
    String longDate;
    String humanDate;
    String activityValue;
    String unit;
    String target;

    public ManualActivity() {
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getPetId() {
        return petId;
    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getLongDate() {
        return longDate;
    }

    public void setLongDate(String longDate) {
        this.longDate = longDate;
    }

    public String getHumanDate() {
        return humanDate;
    }

    public void setHumanDate(String humanDate) {
        this.humanDate = humanDate;
    }

    public String getActivityValue() {
        return activityValue;
    }

    public void setActivityValue(String activityValue) {
        this.activityValue = activityValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
