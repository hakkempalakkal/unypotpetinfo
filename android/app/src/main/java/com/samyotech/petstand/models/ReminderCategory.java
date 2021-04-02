package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by welcome pc on 26-12-2017.
 */

public class ReminderCategory implements Serializable
{
    String name;
    int imgSource;

    public int getImgSource() {
        return imgSource;
    }

    public void setImgSource(int imgSource) {
        this.imgSource = imgSource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
