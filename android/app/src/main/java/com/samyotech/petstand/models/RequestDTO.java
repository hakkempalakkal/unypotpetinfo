package com.samyotech.petstand.models;

import java.io.Serializable;

public class RequestDTO implements Serializable {

    String id = "";
    String user_id = "";
    String pet_id = "";
    String user_id_owner = "";
    String description = "";
    String status = "";
    String created = "";
    String user_owner_name = "";
    String user_name = "";
    PetListDTO petdata;

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

    public String getUser_id_owner() {
        return user_id_owner;
    }

    public void setUser_id_owner(String user_id_owner) {
        this.user_id_owner = user_id_owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public PetListDTO getPetdata() {
        return petdata;
    }

    public void setPetdata(PetListDTO petdata) {
        this.petdata = petdata;
    }
}
