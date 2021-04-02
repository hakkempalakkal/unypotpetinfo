package com.samyotech.petstand.models;

/**
 * Created by welcome pc on 06-01-2018.
 */

public class DaysTotalDTO
{
    public int utc;
    public int totalStpe;
    public int totalKcl;
    public String time;

    public DaysTotalDTO(int utc, int totalStpe, int totalKcl, String time) {
        this.utc = utc;
        this.totalStpe = totalStpe;
        this.totalKcl = totalKcl;
        this.time = time;
    }

    public int getUtc() {
        return utc;
    }

    public void setUtc(int utc) {
        this.utc = utc;
    }

    public int getTotalStpe() {
        return totalStpe;
    }

    public void setTotalStpe(int totalStpe) {
        this.totalStpe = totalStpe;
    }

    public int getTotalKcl() {
        return totalKcl;
    }

    public void setTotalKcl(int totalKcl) {
        this.totalKcl = totalKcl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
