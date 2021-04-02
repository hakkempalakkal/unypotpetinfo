package com.samyotech.petstand.models;

import java.io.Serializable;

public class BreedDTO implements Serializable {

    String id = "";
    String breed_name = "";
    String remark = "";
    String active_status = "";
    String origin = "";
    String life_span = "";
    String weight_male = "";
    String weight_female = "";
    String height_male = "";
    String height_female = "";
    String temperament = "";
    String image_path = "";
    String target = "";
    String manual_activity = "";
    String breed_cat = "";
    String pet_type = "";
    boolean selected = false;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBreed_name() {
        return breed_name;
    }

    public void setBreed_name(String breed_name) {
        this.breed_name = breed_name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getActive_status() {
        return active_status;
    }

    public void setActive_status(String active_status) {
        this.active_status = active_status;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getLife_span() {
        return life_span;
    }

    public void setLife_span(String life_span) {
        this.life_span = life_span;
    }

    public String getWeight_male() {
        return weight_male;
    }

    public void setWeight_male(String weight_male) {
        this.weight_male = weight_male;
    }

    public String getWeight_female() {
        return weight_female;
    }

    public void setWeight_female(String weight_female) {
        this.weight_female = weight_female;
    }

    public String getHeight_male() {
        return height_male;
    }

    public void setHeight_male(String height_male) {
        this.height_male = height_male;
    }

    public String getHeight_female() {
        return height_female;
    }

    public void setHeight_female(String height_female) {
        this.height_female = height_female;
    }

    public String getTemperament() {
        return temperament;
    }

    public void setTemperament(String temperament) {
        this.temperament = temperament;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getManual_activity() {
        return manual_activity;
    }

    public void setManual_activity(String manual_activity) {
        this.manual_activity = manual_activity;
    }

    public String getBreed_cat() {
        return breed_cat;
    }

    public void setBreed_cat(String breed_cat) {
        this.breed_cat = breed_cat;
    }

    public String getPet_type() {
        return pet_type;
    }

    public void setPet_type(String pet_type) {
        this.pet_type = pet_type;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
