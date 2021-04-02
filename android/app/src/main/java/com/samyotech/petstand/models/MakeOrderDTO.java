package com.samyotech.petstand.models;

import java.io.Serializable;

public class MakeOrderDTO implements Serializable {

    String id = "";
    String user_id = "";
    String order_id = "";
    float total_price = 0f;
    float final_price = 0f;
    String status = "";
    float discount = 0f;
    float cod_charges = 0f;
    String order_date = "";
    String place_date = "";
    String address = "";
    String created_at = "";
    String updated_at = "";
    String currency_type = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public float getTotal_price() {
        return total_price;
    }

    public void setTotal_price(float total_price) {
        this.total_price = total_price;
    }

    public float getFinal_price() {
        return final_price;
    }

    public void setFinal_price(float final_price) {
        this.final_price = final_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getCod_charges() {
        return cod_charges;
    }

    public void setCod_charges(float cod_charges) {
        this.cod_charges = cod_charges;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getPlace_date() {
        return place_date;
    }

    public void setPlace_date(String place_date) {
        this.place_date = place_date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }
}
