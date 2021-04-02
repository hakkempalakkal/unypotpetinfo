package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by welcome pc on 06-01-2018.
 */

public class SleepDataDTO  implements Serializable
{
    public long sleeptime;
    public int sleepdata;
    public String marktime;

    public SleepDataDTO(long sleeptime, int sleepdata, String marktime) {
        this.sleeptime = sleeptime;
        this.sleepdata = sleepdata;
        this.marktime = marktime;
    }

    public long getSleeptime() {
        return sleeptime;
    }

    public void setSleeptime(long sleeptime) {
        this.sleeptime = sleeptime;
    }

    public int getSleepdata() {
        return sleepdata;
    }

    public void setSleepdata(int sleepdata) {
        this.sleepdata = sleepdata;
    }

    public String getMarktime() {
        return marktime;
    }

    public void setMarktime(String marktime) {
        this.marktime = marktime;
    }
}
