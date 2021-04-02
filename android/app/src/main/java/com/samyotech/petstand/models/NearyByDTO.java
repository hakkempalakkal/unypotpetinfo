package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by shubham on 24/8/17.
 */

public class NearyByDTO implements Serializable {
    String km = "";

    public NearyByDTO(String km) {
        this.km = km;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }
}
