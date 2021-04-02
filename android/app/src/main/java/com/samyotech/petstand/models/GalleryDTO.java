package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by welcome pc on 24-08-2016.
 */
public class GalleryDTO implements Serializable {

    String id = "";
    String user_id = "";
    String pet_id = "";
    String pet_img_path = "";
    String description = "";
    String created = "";

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

    public String getPet_id() {
        return pet_id;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }

    public String getPet_img_path() {
        return pet_img_path;
    }

    public void setPet_img_path(String pet_img_path) {
        this.pet_img_path = pet_img_path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}
