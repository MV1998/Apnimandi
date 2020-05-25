package com.mohit.varma.apnimandi.model;

import java.io.Serializable;
import java.util.List;

public class Districts implements Serializable {
    private int districtPinCode;
    private String districtName;
    private String districtAbbr;
    private List<String> districtWards;

    public List<String> getDistrictWards() {
        return districtWards;
    }

    public void setDistrictWards(List<String> districtWards) {
        this.districtWards = districtWards;
    }

    public Districts() {
    }

    public int getDistrictPinCode() {
        return districtPinCode;
    }

    public void setDistrictPinCode(int districtPinCode) {
        this.districtPinCode = districtPinCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictAbbr() {
        return districtAbbr;
    }

    public void setDistrictAbbr(String districtAbbr) {
        this.districtAbbr = districtAbbr;
    }

}

