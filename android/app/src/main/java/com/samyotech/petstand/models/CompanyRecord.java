package com.samyotech.petstand.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by welcome pc on 27-01-2017.
 */
public class CompanyRecord implements Serializable {
    String error;
    String message;
    public List<Company>company = new ArrayList<>();

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

    public List<Company> getCompany() {
        return company;
    }

    public void setCompany(List<Company> company) {
        this.company = company;
    }

    public class Company implements Serializable{
        String c_id = "";
        String c_img_path = "";
        String c_name = "";
        public String getC_id() {
            return c_id;
        }

        public void setC_id(String c_id) {
            this.c_id = c_id;
        }

        public String getC_img_path() {
            return c_img_path;
        }

        public void setC_img_path(String c_img_path) {
            this.c_img_path = c_img_path;
        }

        public String getC_name() {
            return c_name;
        }

        public void setC_name(String c_name) {
            this.c_name = c_name;
        }
    }
}
