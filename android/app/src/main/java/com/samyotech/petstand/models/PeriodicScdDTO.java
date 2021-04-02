package com.samyotech.petstand.models;

/**
 * Created by welcome pc on 27-08-2016.
 */
public class PeriodicScdDTO {

    String name;
    int count;
    boolean isChecked;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
