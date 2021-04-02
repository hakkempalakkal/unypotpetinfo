package com.samyotech.petstand.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by welcome pc on 11-12-2017.
 */

public class PetCare  implements Serializable
{
    String error;
    ArrayList<Care> care = new ArrayList<Care>();

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public ArrayList<Care> getCare() {
        return care;
    }

    public void setCare(ArrayList<Care> care) {
        this.care = care;
    }
}
