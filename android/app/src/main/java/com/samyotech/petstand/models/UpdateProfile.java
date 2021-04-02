package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by welcome pc on 17-12-2017.
 */

public class UpdateProfile implements Serializable
{
    String error;
    String message;
    public
    UserDetail userdetail;

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

    public UserDetail getUserdetail() {
        return userdetail;
    }

    public void setUserdetail(UserDetail userdetail) {
        this.userdetail = userdetail;
    }
}
