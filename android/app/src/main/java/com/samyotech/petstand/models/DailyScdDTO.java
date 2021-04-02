package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by welcome pc on 27-08-2016.
 */
public class DailyScdDTO implements Serializable{

    String catName="";
    String title="";
    int drawableLeft;
    String note;
    String rootCategory;

    public String getRootCategory() {
        return rootCategory;
    }

    public void setRootCategory(String rootCategory) {
        this.rootCategory = rootCategory;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDrawableLeft() {
        return drawableLeft;
    }

    public void setDrawableLeft(int drawableLeft) {
        this.drawableLeft = drawableLeft;
    }


    public String getCatName()
    {
        return catName.toLowerCase();
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

}
