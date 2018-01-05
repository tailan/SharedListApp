package com.trucolotecnologia.sharedlistapp.models;

import java.util.ArrayList;



public class SharedList {

    public String title;
    public double totalPrice;
    public ArrayList<Item> items;

    public SharedList(String title) {
        this.title = title;
        this.totalPrice = 0;
    }
}
