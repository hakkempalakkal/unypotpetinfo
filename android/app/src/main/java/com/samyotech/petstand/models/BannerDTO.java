package com.samyotech.petstand.models;

import java.io.Serializable;

public class BannerDTO implements Serializable {

    String b_image = "";
    String  id = "";
    String  status = "";

    public String getB_image() {
        return b_image;
    }

    public void setB_image(String b_image) {
        this.b_image = b_image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
