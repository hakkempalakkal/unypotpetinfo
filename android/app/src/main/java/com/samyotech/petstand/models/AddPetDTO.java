package com.samyotech.petstand.models;

/**
 * Created by mayank on 21/3/18.
 */

public class AddPetDTO {
    int status;
    String message = "";

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
