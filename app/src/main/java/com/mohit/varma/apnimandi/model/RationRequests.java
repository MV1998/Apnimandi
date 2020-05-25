package com.mohit.varma.apnimandi.model;

import java.io.Serializable;

public class RationRequests implements Serializable {
    private String rationRequestCode;
    private String requestTime;
    private boolean requestStatus;

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public boolean isRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(boolean requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRationRequestCode() {
        return rationRequestCode;
    }

    public void setRationRequestCode(String rationRequestCode) {
        this.rationRequestCode = rationRequestCode;
    }
}
