package com.samyotech.petstand.models;

import java.io.Serializable;

public class BrandsDTO implements Serializable {

  String c_img_path="";
  String cat_id ="";
  String c_desc="";
  String c_id="";
  String status="";
  String sub_cat_id="";
  String c_name="";

    public String getC_img_path() {
        return c_img_path;
    }

    public void setC_img_path(String c_img_path) {
        this.c_img_path = c_img_path;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getC_desc() {
        return c_desc;
    }

    public void setC_desc(String c_desc) {
        this.c_desc = c_desc;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSub_cat_id() {
        return sub_cat_id;
    }

    public void setSub_cat_id(String sub_cat_id) {
        this.sub_cat_id = sub_cat_id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }
}
