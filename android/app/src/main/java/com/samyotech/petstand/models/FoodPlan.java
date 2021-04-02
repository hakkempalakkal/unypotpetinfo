package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by welcome pc on 31-01-2017.
 */
public class FoodPlan extends BaseDTO implements Serializable
{
    String productId;
    String productName;
    String comId;
    String comName;
    String quantity = "0";
    String unit;
    String PetId;

    public FoodPlan() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getComId() {
        return comId;
    }

    public void setComId(String comId) {
        this.comId = comId;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPetId() {
        return PetId;
    }

    public void setPetId(String petId) {
        PetId = petId;
    }
}
