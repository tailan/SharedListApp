package com.trucolotecnologia.sharedlistapp.models;

import java.util.ArrayList;



public class User {
    public String email;
    public String name;
    public ArrayList<SharedList> lists;


    public User(String name, String mail) {
        this.email = mail;
        this.name = name;
    }

    public String getUID()
    {
        return this.email.split("@")[0].replaceAll("\\W", "");
    }
}
