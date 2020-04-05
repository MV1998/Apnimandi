package com.mohit.varma.apnimandi.model;

import java.io.Serializable;

public class UserAddress implements Serializable {

    private String userName;
    private String phoneNumber;
    private String AddressLine1;
    private String AddressLine2;
    private String cityCode;

    public UserAddress() {
    }

    public UserAddress(String userName, String phoneNumber, String addressLine1, String addressLine2, String cityCode) {
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        AddressLine1 = addressLine1;
        AddressLine2 = addressLine2;
        this.cityCode = cityCode;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressLine1() {
        return AddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        AddressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return AddressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        AddressLine2 = addressLine2;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
