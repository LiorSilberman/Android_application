package com.example.reusethings;

import android.graphics.Bitmap;

public class Item {
    private String name;
    private String phoneNumber;
    private String address;
    private Bitmap image;
    private String username;

    public Item(String name, String phoneNumber, String address, Bitmap image, String username) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.image = image;
        this.username = username;
    }

    public String getName() {
        return name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getAddress() {
        return address;
    }
    public Bitmap getImage() {
        return image;
    }
    public String getUserName() {
        return username;
    }

}

