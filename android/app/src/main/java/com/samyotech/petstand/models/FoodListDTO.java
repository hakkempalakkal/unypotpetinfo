package com.samyotech.petstand.models;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by mayank on 16/2/18.
 */

public class FoodListDTO implements Serializable {

    String p_id = "";
    String p_code = "";
    String c_name = "";
    String p_name = "";
    String p_description = "";
    String p_type = "";
    String p_pet_type = "";
    String p_cat_type = "";
    float p_rate = 0f;
    float p_rate_sale = 0f;
    String quantity = "";
    float gross_amt = 0f;
    String discount = "";
    float net_amt = 0f;
    String img_path = "";
    String status = "";
    String product_rating = "";
    String currency_type = "";
    String shipping_cost = "";
    String final_amount = "";
    String weight = "";
    String c_id = "";
    ArrayList<ProductReview> reviewlist = new ArrayList<>();
    ArrayList<Image> proImage = new ArrayList<>();
    ArrayList<String> color = new ArrayList<>();
    ArrayList<String> size = new ArrayList<>();


    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(String final_amount) {
        this.final_amount = final_amount;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getP_code() {
        return p_code;
    }

    public void setP_code(String p_code) {
        this.p_code = p_code;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
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

    public String getP_type() {
        return p_type;
    }

    public void setP_type(String p_type) {
        this.p_type = p_type;
    }

    public String getP_pet_type() {
        return p_pet_type;
    }

    public void setP_pet_type(String p_pet_type) {
        this.p_pet_type = p_pet_type;
    }

    public String getP_cat_type() {
        return p_cat_type;
    }

    public void setP_cat_type(String p_cat_type) {
        this.p_cat_type = p_cat_type;
    }

    public float getP_rate() {
        return p_rate;
    }

    public void setP_rate(float p_rate) {
        this.p_rate = p_rate;
    }

    public float getP_rate_sale() {
        return p_rate_sale;
    }

    public void setP_rate_sale(float p_rate_sale) {
        this.p_rate_sale = p_rate_sale;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public float getGross_amt() {
        return gross_amt;
    }

    public void setGross_amt(float gross_amt) {
        this.gross_amt = gross_amt;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public float getNet_amt() {
        return net_amt;
    }

    public void setNet_amt(float net_amt) {
        this.net_amt = net_amt;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setImg_path(String img_path) {
        this.img_path = img_path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProduct_rating() {
        return product_rating;
    }

    public String getCurrency_type() {
        return currency_type;
    }

    public void setCurrency_type(String currency_type) {
        this.currency_type = currency_type;
    }

    public void setProduct_rating(String product_rating) {
        this.product_rating = product_rating;
    }

    public ArrayList<ProductReview> getReviewlist() {
        return reviewlist;
    }

    public void setReviewlist(ArrayList<ProductReview> reviewlist) {
        this.reviewlist = reviewlist;
    }

    public ArrayList<Image> getProImage() {
        return proImage;
    }

    public void setProImage(ArrayList<Image> proImage) {
        this.proImage = proImage;
    }

    public ArrayList<String> getColor() {
        return color;
    }

    public void setColor(ArrayList<String> color) {
        this.color = color;
    }

    public ArrayList<String> getSize() {
        return size;
    }

    public void setSize(ArrayList<String> size) {
        this.size = size;
    }

    public String getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(String shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public class Image implements Serializable {
        String media_id = "";
        String image = "";

        public String getMedia_id() {
            return media_id;
        }

        public void setMedia_id(String media_id) {
            this.media_id = media_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

}
