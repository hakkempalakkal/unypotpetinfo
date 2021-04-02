package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by mayank on 16/2/18.
 */

public class PetCatList implements Serializable {

        String id = "";
        String pet_name = "";
        String pet_image = "";
        String created_at = "";

    public PetCatList(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPet_name() {
            return pet_name;
        }

        public void setPet_name(String pet_name) {
            this.pet_name = pet_name;
        }

        public String getPet_image() {
            return pet_image;
        }

        public void setPet_image(String pet_image) {
            this.pet_image = pet_image;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

    @Override
    public String toString() {
        return pet_name.toString();
    }
}

