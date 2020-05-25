package com.mohit.varma.apnimandi.model;

import java.io.Serializable;
import java.util.List;

public class IndianState implements Serializable {
    private static final int TOTAL_STATE = 28;
    private String stateName;
    private int stateCode;
    private String stateAbbr;
    private List<Districts> districtsList;
    private int numberOfDistricts;

    public IndianState() {
    }
    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public List<Districts> getDistrictsList() {
        return districtsList;
    }

    public void setDistrictsList(List<Districts> districtsList) {
        this.districtsList = districtsList;
    }

    public String getStateAbbr() {
        return stateAbbr;
    }

    public void setStateAbbr(String stateAbbr) {
        this.stateAbbr = stateAbbr;
    }

    public int getNumberOfDistricts() {
        return numberOfDistricts;
    }

    public void setNumberOfDistricts(int numberOfDistricts) {
        this.numberOfDistricts = numberOfDistricts;
    }

    public static int getTotalState() {
        return TOTAL_STATE;
    }
}