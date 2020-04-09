package com.mohit.varma.apnimandi.model;

import java.io.Serializable;

public class Delivery implements Serializable {
    private int orderId;
    private String whenAssigned;
    private DeliveryBoyInfo deliveryBoyInfo;
    private String whenDelivered;
}
