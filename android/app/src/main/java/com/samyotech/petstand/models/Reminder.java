
package com.samyotech.petstand.models;


import java.io.Serializable;

public class Reminder implements Serializable{

    private String id;
    private String timing;
    private String user_id;
    private String category;
    private String date_string;
    private String appointment_date;
    private String appointment_timestamp;
    private int status;
    private String repeat;
    private String advance;
    private String remark;
    private String pet_id;
    private String timezone;
    private String pet_name;
    private String pet_img_path;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTiming() {
        return timing;
    }

    public void setTiming(String timing) {
        this.timing = timing;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate_string() {
        return date_string;
    }

    public void setDate_string(String date_string) {
        this.date_string = date_string;
    }

    public String getAppointment_date() {
        return appointment_date;
    }

    public void setAppointment_date(String appointment_date) {
        this.appointment_date = appointment_date;
    }

    public String getAppointment_timestamp() {
        return appointment_timestamp;
    }

    public void setAppointment_timestamp(String appointment_timestamp) {
        this.appointment_timestamp = appointment_timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getAdvance() {
        return advance;
    }

    public void setAdvance(String advance) {
        this.advance = advance;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPet_id() {
        return pet_id;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getPet_img_path() {
        return pet_img_path;
    }

    public void setPet_img_path(String pet_img_path) {
        this.pet_img_path = pet_img_path;
    }
}
