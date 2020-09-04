package com.example.postie11.Models;

public class PostModel {
    private String profile,username,photo,time,caption,likeCount,commentCount,shareCount,unlikeCount;

    public PostModel(String profile, String username, String photo, String time, String caption, String likeCount, String commentCount, String shareCount,String unlikeCount) {
        this.profile = profile;
        this.username = username;
        this.photo = photo;
        this.time = time;
        this.caption = caption;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.shareCount = shareCount;
        this.unlikeCount= unlikeCount;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(String likeCount) {
        this.likeCount = likeCount;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getShareCount() {
        return shareCount;
    }

    public void setShareCount(String shareCount) {
        this.shareCount = shareCount;
    }

    public void unlikeCount(String unlikeCount) {
        this.unlikeCount = unlikeCount;
    }
}
