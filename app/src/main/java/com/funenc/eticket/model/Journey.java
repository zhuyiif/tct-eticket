package com.funenc.eticket.model;

public class Journey {
    private int id;
    private int userId;
    private String phone;
    private String inStationName;
    private String inDealTime;
    private String outStationName;
    private String outDealTime;
    private String matchResult;
    private String matchTime;
    private int deductFee;
    private String inStationNumber;
    private String outStationNumber;
    private int week;
    private String status;
    private String createdAt;
    private String updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInStationName() {
        return inStationName;
    }

    public void setInStationName(String inStationName) {
        this.inStationName = inStationName;
    }

    public String getInDealTime() {
        return inDealTime;
    }

    public void setInDealTime(String inDealTime) {
        this.inDealTime = inDealTime;
    }

    public String getOutStationName() {
        return outStationName;
    }

    public void setOutStationName(String outStationName) {
        this.outStationName = outStationName;
    }

    public String getOutDealTime() {
        return outDealTime;
    }

    public void setOutDealTime(String outDealTime) {
        this.outDealTime = outDealTime;
    }

    public String getMatchResult() {
        return matchResult;
    }

    public void setMatchResult(String matchResult) {
        this.matchResult = matchResult;
    }

    public String getMatchTime() {
        return matchTime;
    }

    public void setMatchTime(String matchTime) {
        this.matchTime = matchTime;
    }

    public int getDeductFee() {
        return deductFee;
    }

    public void setDeductFee(int deductFee) {
        this.deductFee = deductFee;
    }

    public String getInStationNumber() {
        return inStationNumber;
    }

    public void setInStationNumber(String inStationNumber) {
        this.inStationNumber = inStationNumber;
    }

    public String getOutStationNumber() {
        return outStationNumber;
    }

    public void setOutStationNumber(String outStationNumber) {
        this.outStationNumber = outStationNumber;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
