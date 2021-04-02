package com.samyotech.petstand.models;

/**
 * Created by welcome pc on 27-11-2017.
 */

import java.io.Serializable;
import java.util.ArrayList;

public class User  implements Serializable {
    public boolean error = false;
    public String message = "";
    public UserDetail userdetail;
    public Pets pets;


    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Pets getPets() {
        return pets;
    }

    public void setPets(Pets pets) {
        this.pets = pets;
    }

    public UserDetail getUserDetail() {
        return userdetail;
    }

    public void setUserDetail(UserDetail userdetail) {
        this.userdetail = userdetail;
    }

    public static class Pets {
        boolean error = false;
        ArrayList<Petdetail> petdetail = new ArrayList<>();

        public boolean isError() {
            return error;
        }

        public void setError(boolean error) {
            this.error = error;
        }

        public ArrayList<Petdetail> getPetdetail() {
            return petdetail;
        }

        public void setPetdetail(ArrayList<Petdetail> petdetail) {
            this.petdetail = petdetail;
        }

    }


}
