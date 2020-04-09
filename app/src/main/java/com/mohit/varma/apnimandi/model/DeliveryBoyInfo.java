package com.mohit.varma.apnimandi.model;

import java.io.Serializable;

public class DeliveryBoyInfo implements Serializable {
    private int deliveryBoyId;
    private String deliveryBoyName;
    private String deliveryBoyImage;
    private String deliveryBoyAge;
    private String deliveryArea;

    public int getDeliveryBoyId() {
        return deliveryBoyId;
    }

    public void setDeliveryBoyId(int deliveryBoyId) {
        this.deliveryBoyId = deliveryBoyId;
    }

    public String getDeliveryBoyName() {
        return deliveryBoyName;
    }

    public void setDeliveryBoyName(String deliveryBoyName) {
        this.deliveryBoyName = deliveryBoyName;
    }

    public String getDeliveryBoyImage() {
        return deliveryBoyImage;
    }

    public void setDeliveryBoyImage(String deliveryBoyImage) {
        this.deliveryBoyImage = deliveryBoyImage;
    }

    public String getDeliveryBoyAge() {
        return deliveryBoyAge;
    }

    public void setDeliveryBoyAge(String deliveryBoyAge) {
        this.deliveryBoyAge = deliveryBoyAge;
    }

    public String getDeliveryArea() {
        return deliveryArea;
    }

    public void setDeliveryArea(String deliveryArea) {
        this.deliveryArea = deliveryArea;
    }
}
