package com.example.eticket.ui.pojo;

import java.util.Date;

public class JourneyHistoryItem {
    public enum JourneyStatus{
        PAYED, GOING, TO_PAY, EXCEPTION
    }
    public static class Builder {
        JourneyHistoryItem item = new JourneyHistoryItem();
        public Builder journeyDate(Date date){
            item.setJourneyDate(date);
            return this;
        }
        public Builder inStationName(String name){
            item.setInStationName(name);
            return this;
        }
        public Builder outStationName(String name){
            item.setOutStationName(name);
            return this;
        }
        public Builder status(JourneyStatus status){
            item.setStatus(status);
            return this;
        }
        public Builder inDealTime(String time){
            item.setInDealTime(time);
            return this;
        }
        public Builder outDealTime(String time){
            item.setOutDealTime(time);
            return this;
        }
        public JourneyHistoryItem build(){
            return item;
        }
    }
    private Date journeyDate;
    private String inStationName;
    private String outStationName;
    private JourneyStatus status;
    private String inDealTime;
    private String outDealTime;

    public Date getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(Date journeyDate) {
        this.journeyDate = journeyDate;
    }

    public String getInStationName() {
        return inStationName;
    }

    public void setInStationName(String inStationName) {
        this.inStationName = inStationName;
    }

    public String getOutStationName() {
        return outStationName;
    }

    public void setOutStationName(String outStationName) {
        this.outStationName = outStationName;
    }

    public JourneyStatus getStatus() {
        return status;
    }

    public void setStatus(JourneyStatus status) {
        this.status = status;
    }

    public String getInDealTime() {
        return inDealTime;
    }

    public void setInDealTime(String inDealTime) {
        this.inDealTime = inDealTime;
    }

    public String getOutDealTime() {
        return outDealTime;
    }

    public void setOutDealTime(String outDealTime) {
        this.outDealTime = outDealTime;
    }
}
