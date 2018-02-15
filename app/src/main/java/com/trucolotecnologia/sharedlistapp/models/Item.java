package com.trucolotecnologia.sharedlistapp.models;



public class Item {

    public String description;
    public boolean isChecked;
    public double price;

    public Item(String description) {
        this.description = description;
        this.isChecked = false;
        this.price = 0.0;
    }
}
