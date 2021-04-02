package com.samyotech.petstand.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by welcome pc on 29-01-2017.
 */
public class ProductRecord {
    String error;
    String message;
    List<Product> all_product = new ArrayList<>();

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Product> getAll_product() {
        return all_product;
    }

    public void setAll_product(List<Product> all_product) {
        this.all_product = all_product;
    }

    public class Product {
        String c_id;
        String c_name;
        String img_path;
        String isadult;
        String p_id;
        String p_name;
        String breedCategories;
        String threshold;
        String unit;

        public String getThreshold() {
            return threshold;
        }

        public void setThreshold(String threshold) {
            this.threshold = threshold;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getBreedCategories() {
            return breedCategories;
        }

        public void setBreedCategories(String breedCategories) {
            this.breedCategories = breedCategories;
        }

        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

        public String getC_name() {
            return c_name;
        }

        public void setC_name(String c_name) {
            this.c_name = c_name;
        }

        public String getImg_path() {
            return img_path;
        }

        public void setImg_path(String img_path) {
            this.img_path = img_path;
        }

        public String getIsadult() {
            return isadult;
        }

        public void setIsadult(String isadult) {
            this.isadult = isadult;
        }

        public String getP_id() {
            return p_id;
        }

        public void setP_id(String p_id) {
            this.p_id = p_id;
        }

        public String getP_name() {
            return p_name;
        }

        public void setP_name(String p_name) {
            this.p_name = p_name;
        }
    }


    public ProductRecord() {
    }


}
