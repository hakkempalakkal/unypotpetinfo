package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by welcome pc on 22-11-2017.
 */

public class RealCount implements Serializable
{
    public String steptime;
    public int stepdata;
    public int caloriedata;
    public String marktime;
    public int utctime;

    public RealCount(String steptime, int stepdata, int caloriedata, String marktime, int utctime) {
        this.steptime = steptime;
        this.stepdata = stepdata;
        this.caloriedata = caloriedata;
        this.marktime = marktime;
        this.utctime = utctime;
    }

    public String getSteptime() {
        return steptime;
    }

    public void setSteptime(String steptime) {
        this.steptime = steptime;
    }

    public int getStepdata() {
        return stepdata;
    }

    public void setStepdata(int stepdata) {
        this.stepdata = stepdata;
    }

    public int getCaloriedata() {
        return caloriedata;
    }

    public void setCaloriedata(int caloriedata) {
        this.caloriedata = caloriedata;
    }

    public String getMarktime() {
        return marktime;
    }

    public void setMarktime(String marktime) {
        this.marktime = marktime;
    }

    public int getUtctime() {
        return utctime;
    }

    public void setUtctime(int utctime) {
        this.utctime = utctime;
    }
}
