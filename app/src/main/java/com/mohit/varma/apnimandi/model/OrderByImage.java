package com.mohit.varma.apnimandi.model;

import java.io.Serializable;

public class OrderByImage implements Serializable {
    private String phoneNumber;
    private String imageURL;

    public OrderByImage() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
