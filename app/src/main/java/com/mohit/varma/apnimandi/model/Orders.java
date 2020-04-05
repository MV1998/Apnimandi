package com.mohit.varma.apnimandi.model;

import java.util.List;

public class Orders {
    private int orderId;
    private String orderDate;
    private UserAddress userAddress;
    private List<UCart> uCartList;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private long grandTotal;

    public Orders() {
    }

    public Orders(int orderId, String orderDate, UserAddress userAddress, List<UCart> uCartList, OrderStatus orderStatus, PaymentMethod paymentMethod, PaymentStatus paymentStatus, long grandTotal) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.userAddress = userAddress;
        this.uCartList = uCartList;
        this.orderStatus = orderStatus;
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
        this.grandTotal = grandTotal;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public List<UCart> getuCartList() {
        return uCartList;
    }

    public void setuCartList(List<UCart> uCartList) {
        this.uCartList = uCartList;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public long getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(long grandTotal) {
        this.grandTotal = grandTotal;
    }
}
