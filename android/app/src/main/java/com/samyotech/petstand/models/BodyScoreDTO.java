package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by welcome pc on 25-01-2017.
 */
public class BodyScoreDTO implements Serializable {
    int index = 0;
    boolean status = false;
    String text = "";
    int imageId = 0;


    public BodyScoreDTO(int index, boolean status, String text) {
        this.index = index;
        this.status = status;
        this.text = text;
    }

    public BodyScoreDTO() {
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
