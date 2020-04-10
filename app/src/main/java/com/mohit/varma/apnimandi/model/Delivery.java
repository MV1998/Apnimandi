package com.mohit.varma.apnimandi.model;

import java.io.Serializable;

public class Delivery implements Serializable {
    private int orderId;
    private String whenAssigned;
    private DeliveryBoyInfo deliveryBoyInfo;
    private String whenDelivered;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getWhenAssigned() {
        return whenAssigned;
    }

    public void setWhenAssigned(String whenAssigned) {
        this.whenAssigned = whenAssigned;
    }

    public DeliveryBoyInfo getDeliveryBoyInfo() {
        return deliveryBoyInfo;
    }

    public void setDeliveryBoyInfo(DeliveryBoyInfo deliveryBoyInfo) {
        this.deliveryBoyInfo = deliveryBoyInfo;
    }

    public String getWhenDelivered() {
        return whenDelivered;
    }

    public void setWhenDelivered(String whenDelivered) {
        this.whenDelivered = whenDelivered;
    }
}
