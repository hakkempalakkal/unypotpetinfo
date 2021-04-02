package com.samyotech.petstand.models;

import java.io.Serializable;

public class NearByDTO implements Serializable {


    String id = "";
    String nearby_id = "";
    String name = "";
    String image_path = "";
    String email = "";
    String mobile = "";
    String address = "";
    String start_timing = "";
    String end_timing = "";
    String lati = "";
    String longi = "";
    String status = "";
    String e_open_time = "";
    String e_close_time = "";

    public String getE_open_time() {
        return e_open_time;
    }

    public void setE_open_time(String e_open_time) {
        this.e_open_time = e_open_time;
    }

    public String getE_close_time() {
        return e_close_time;
    }

    public void setE_close_time(String e_close_time) {
        this.e_close_time = e_close_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNearby_id() {
        return nearby_id;
    }

    public void setNearby_id(String nearby_id) {
        this.nearby_id = nearby_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStart_timing() {
        return start_timing;
    }

    public void setStart_timing(String start_timing) {
        this.start_timing = start_timing;
    }

    public String getEnd_timing() {
        return end_timing;
    }

    public void setEnd_timing(String end_timing) {
        this.end_timing = end_timing;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
