package com.samyotech.petstand.models;


import java.io.Serializable;

public class PetCategoryDTO implements Serializable {

    String id = "";
    String cat_title = "";
    String cat_desc = "";
    String c_img_path = "";
    String status = "";
    String created_at = "";
    String updated_on = "";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCat_title() {
        return cat_title;
    }

    public void setCat_title(String cat_title) {
        this.cat_title = cat_title;
    }

    public String getCat_desc() {
        return cat_desc;
    }

    public void setCat_desc(String cat_desc) {
        this.cat_desc = cat_desc;
    }

    public String getC_img_path() {
        return c_img_path;
    }

    public void setC_img_path(String c_img_path) {
        this.c_img_path = c_img_path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_on() {
        return updated_on;
    }

    public void setUpdated_on(String updated_on) {
        this.updated_on = updated_on;
    }

    @Override
    public String toString() {
        return cat_title.toString();
    }
}
