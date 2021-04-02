package com.samyotech.petstand.models;

import java.io.Serializable;

public class ChatListDTO implements Serializable {

    String id = "";
    String user_id = "";
    String user_id_receiver = "";
    String message = "";
    String sender_name = "";
    String date = "";
    String media = "";
    String chat_type = "";
    String thumb = "";
    String latitude = "";
    String longitude = "";
    String chat_state = "";
    String userName = "";
    String userImage = "";


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

    public String getUser_id_receiver() {
        return user_id_receiver;
    }

    public void setUser_id_receiver(String user_id_receiver) {
        this.user_id_receiver = user_id_receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getChat_type() {
        return chat_type;
    }

    public void setChat_type(String chat_type) {
        this.chat_type = chat_type;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getChat_state() {
        return chat_state;
    }

    public void setChat_state(String chat_state) {
        this.chat_state = chat_state;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
