package com.mohit.varma.apnimandi.model;

import java.io.Serializable;
import java.util.List;

public class RUser implements Serializable {
    private String userId;
    private String name;
    private String userState;
    private String userDistrict;
    private String userWardNumber;
    private String phoneNumber;
    private String rationCardNumber;
    private List<RationRequests> rationRequests;
    private int numberOfRequest;

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getUserDistrict() {
        return userDistrict;
    }

    public void setUserDistrict(String userDistrict) {
        this.userDistrict = userDistrict;
    }

    public String getUserWardNumber() {
        return userWardNumber;
    }

    public void setUserWardNumber(String userWardNumber) {
        this.userWardNumber = userWardNumber;
    }

    public List<RationRequests> getRationRequests() {
        return rationRequests;
    }

    public void setRationRequests(List<RationRequests> rationRequests) {
        this.rationRequests = rationRequests;
    }

    public int getNumberOfRequest() {
        return numberOfRequest;
    }

    public void setNumberOfRequest(int numberOfRequest) {
        this.numberOfRequest = numberOfRequest;
    }

    public String getRationWardNumber() {
        return userWardNumber;
    }

    public void setRationWardNumber(String userWardNumber) {
        this.userWardNumber = userWardNumber;
    }

    public String getRationCardNumber() {
        return rationCardNumber;
    }

    public void setRationCardNumber(String rationCardNumber) {
        this.rationCardNumber = rationCardNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
