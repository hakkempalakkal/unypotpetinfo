package com.samyotech.petstand.models;

import java.io.Serializable;

public class CartDTO implements Serializable {
    String id = "";
    String user_id = "";
    String product_id = "";
    String quantity = "";
    String created_at = "";
    String updated_at = "";
    String color = "";
    String size = "";
    String p_name = "";
    String p_description = "";
    float p_rate = 0f;
    float discount = 0f;
    String weight = "";
    float price_dicount = 0f;
    String img_path = "";
    String currency_type = "";
    String shipping_cost = "";
    float product_total_price = 0f;
    String mandatory = "";
    String food_product_quantity = "";

    public String getFood_product_quantity() {
        return food_product_quantity;
    }

    public void setFood_product_quantity(String food_product_quantity) {
        this.food_product_quantity = food_product_quantity;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
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

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public String getP_description() {
        return p_description;
    }

    public void setP_description(String p_description) {
        this.p_description = p_description;
    }

    public float getP_rate() {
        return p_rate;
    }

    public void setP_rate(float p_rate) {
        this.p_rate = p_rate;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getPrice_dicount() {
        return price_dicount;
    }

    public void setPrice_dicount(float price_dicount) {
        this.price_dicount = price_dicount;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public float getProduct_total_price() {
        return product_total_price;
    }

    public void setProduct_total_price(float product_total_price) {
        this.product_total_price = product_total_price;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(String shipping_cost) {
        this.shipping_cost = shipping_cost;
    }
}
