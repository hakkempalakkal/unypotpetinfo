package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by varunverma on 20/1/18.
 */

public class PublicWallDTO implements Serializable {
    String postID = "";
    String content = "";
    String title = "";
    String postType = "";
    String media = "";
    String public_id = "";
    String user_id = "";
    String createAt = "";
    String flag = "";
    int comunity_id = 0;
    boolean is_like = false;
    String user_image = "";
    String user_name = "";
    int likes = 0;
    String comments = "";
    String thumb_image = "";
    boolean is_abuse = false;

    public int getComunity_id() {
        return comunity_id;
    }

    public void setComunity_id(int comunity_id) {
        this.comunity_id = comunity_id;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getPublic_id() {
        return public_id;
    }

    public void setPublic_id(String public_id) {
        this.public_id = public_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public boolean isIs_like() {
        return is_like;
    }

    public void setIs_like(boolean is_like) {
        this.is_like = is_like;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public boolean isIs_abuse() {
        return is_abuse;
    }

    public void setIs_abuse(boolean is_abuse) {
        this.is_abuse = is_abuse;
    }
}
