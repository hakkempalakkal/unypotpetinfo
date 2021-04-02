package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by Administrator on 10/17/2016.
 */
public class Insurance extends BaseDTO implements Serializable
{
    String ins_provider = "";
    String ins_policy_no = "";
    String ins_renewal_date = "";
    String pet_ins_path = "";
    String pet_id = "";
    String insId = "";
    String longVal = "";

    public String getLongVal() {
        return longVal;
    }

    public void setLongVal(String longVal) {
        this.longVal = longVal;
    }

    public String getInsId() {
        return insId;
    }

    public void setInsId(String insId) {
        this.insId = insId;
    }

    public String getIns_provider() {
        return ins_provider;
    }

    public void setIns_provider(String ins_provider) {
        this.ins_provider = ins_provider;
    }

    public String getIns_policy_no() {
        return ins_policy_no;
    }

    public void setIns_policy_no(String ins_policy_no) {
        this.ins_policy_no = ins_policy_no;
    }

    public String getIns_renewal_date() {
        return ins_renewal_date;
    }

    public void setIns_renewal_date(String ins_renewal_date) {
        this.ins_renewal_date = ins_renewal_date;
    }

    public String getPet_ins_path() {
        return pet_ins_path;
    }

    public void setPet_ins_path(String pet_ins_path) {
        this.pet_ins_path = pet_ins_path;
    }

    public String getPet_id() {
        return pet_id;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }
}
