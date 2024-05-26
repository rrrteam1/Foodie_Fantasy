package com.raja.resturant.Model;

import android.content.Context;

public class UserAuthModel {

    private String user_name;
    private String user_mail;

    public UserAuthModel() {
    }

    public UserAuthModel(String user_name, String user_mail) {
        this.user_name = user_name;
        this.user_mail = user_mail;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_mail() {
        return user_mail;
    }

    public void setUser_mail(String user_mail) {
        this.user_mail = user_mail;
    }

}
