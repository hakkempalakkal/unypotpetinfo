package com.samyotech.petstand.models;

import java.io.Serializable;

public class SendShopifyProductDTO implements Serializable {


    String variant_id = "";
    String quantity = "";
    String title = "";
    String price = "";

    public SendShopifyProductDTO(String variant_id, String quantity, String title, String price) {
        this.variant_id = variant_id;
        this.quantity = quantity;
        this.title = title;
        this.price = price;
    }

    public String getVariant_id() {
        return variant_id;
    }

    public void setVariant_id(String variant_id) {
        this.variant_id = variant_id;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "variant_id='" + variant_id + '\'' +
                        ", quantity='" + quantity + '\'' +
                        ", title='" + title + '\'' +
                        ", price='" + price + '\'';

    }
}
