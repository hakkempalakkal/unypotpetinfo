package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by shubham on 24/8/17.
 */

public class GenderDTO implements Serializable {
    String gender = "";

    public GenderDTO(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
