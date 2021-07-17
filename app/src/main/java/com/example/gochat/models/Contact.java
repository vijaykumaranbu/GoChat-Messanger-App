package com.example.gochat.models;

public class Contact{
    private int userId;
    private String userName;
    private String message;
    private String date;
    private String profileUrl;

    public Contact(int userId, String userName, String message, String date, String profileUrl) {
        this.userId = userId;
        this.userName = userName;
        this.message = message;
        this.date = date;
        this.profileUrl = profileUrl;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getProfileUrl() {
        return profileUrl;
    }
}
