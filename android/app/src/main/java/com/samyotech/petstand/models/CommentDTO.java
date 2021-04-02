package com.samyotech.petstand.models;

import java.io.Serializable;

/**
 * Created by varunverma on 23/1/18.
 */

public class CommentDTO implements Serializable {

    String commentID = "";
    String user_id = "";
    String postID = "";
    String content = "";
    String createAt = "";
    String flag = "";
    String userName = "";
    String image = "";

    public CommentDTO(String commentID, String user_id, String postID, String content, String createAt, String flag, String userName, String image) {
        this.commentID = commentID;
        this.user_id = user_id;
        this.postID = postID;
        this.content = content;
        this.createAt = createAt;
        this.flag = flag;
        this.userName = userName;
        this.image = image;
    }

    public String getCommentID() {
        return commentID;
    }

    public void setCommentID(String commentID) {
        this.commentID = commentID;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
