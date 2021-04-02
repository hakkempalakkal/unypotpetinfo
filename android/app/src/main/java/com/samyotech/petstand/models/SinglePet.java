package com.samyotech.petstand.models;

/**
 * Created by welcome pc on 21-12-2017.
 */

public class SinglePet
{
    String error;
    String message;
    Petdetail pet;

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

    public Petdetail getPet() {
        return pet;
    }

    public void setPet(Petdetail pet) {
        this.pet = pet;
    }
}
