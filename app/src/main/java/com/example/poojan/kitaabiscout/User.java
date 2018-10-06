package com.example.poojan.kitaabiscout;

import java.util.HashMap;

public class User {
    private String userName;
    private String userEmail;
    private String userImgUrl;
    private String favGenre1;
    private String favGenre2;

    public User(String userName, String userEmail, String userImgUrl, String favGenre1, String favGenre2) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userImgUrl = userImgUrl;
        this.favGenre1 = favGenre1;
        this.favGenre2 = favGenre2;
    }

    public User() {}

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserImgUrl() {
        return userImgUrl;
    }

    public void setUserImgUrl(String userImgUrl) {
        this.userImgUrl = userImgUrl;
    }

    public String getFavGenre1() {
        return favGenre1;
    }

    public void setFavGenre1(String favGenre1) {
        this.favGenre1 = favGenre1;
    }

    public String getFavGenre2() {
        return favGenre2;
    }

    public void setFavGenre2(String favGenre2) {
        this.favGenre2 = favGenre2;
    }
}
