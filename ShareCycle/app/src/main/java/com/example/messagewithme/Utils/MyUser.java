package com.example.messagewithme.Utils;

public class MyUser {

    private  String username;
    private String phonenumbar;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhonenumbar() {
        return phonenumbar;
    }

    public void setPhonenumbar(String phonenumbar) {
        this.phonenumbar = phonenumbar;
    }

    public MyUser(String username, String phonenumbar) {
        this.username = username;
        this.phonenumbar = phonenumbar;
    }
}
