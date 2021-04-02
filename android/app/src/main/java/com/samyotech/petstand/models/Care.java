package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by welcome pc on 28-08-2016.
 */
public class Care implements Serializable {

    String id = "";
    String formattedTime = "";
    String longTime = "";
    String ownerEmail = "";
    String isDone = "";
    String petId = "";
    String cateName = "";
    String formattedDate = "";
    String interval = "";
    String note = "";
    String isNone = "";
    String title = "";
    String isEnable = "";

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public String getCateName() {
        return cateName;
    }

    public void setCateName(String cateName) {
        this.cateName = cateName;
    }


    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getIsNone() {
        return isNone;
    }

    public void setIsNone(String isNone) {
        this.isNone = isNone;
    }

    public String getPetId() {
        return petId;

    }

    public void setPetId(String petId) {
        this.petId = petId;
    }

    public String getIsDone() {
        return isDone;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }


    public String getId() {
        return id;
    }

    public String getFormattedTime() {
        return formattedTime;
    }

    public void setFormattedTime(String formattedTime) {
        this.formattedTime = formattedTime;
    }

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getLongTime() {
        return longTime;
    }

    public void setLongTime(String longTime) {
        this.longTime = longTime;
    }
}
