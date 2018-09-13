package com.funenc.eticket.ui.pojo;

import android.content.res.Resources;

import com.funenc.eticket.R;
import com.funenc.eticket.engine.AppEngine;
import com.funenc.eticket.util.StringUtils;

import java.util.Date;

public class JourneyHistoryItem {
    public enum JourneyStatus {
        COMPLETE(AppEngine.getSystemContext().getString(R.string.complete)),
        PAYED(AppEngine.getSystemContext().getString(R.string.payed)),
        GOING(AppEngine.getSystemContext().getString(R.string.going)),
        TO_PAY(AppEngine.getSystemContext().getString(R.string.to_pay)),
        EXCEPTION(AppEngine.getSystemContext().getString(R.string.exception));
        String statusDesc;

        private JourneyStatus(String statusDesc) {
            this.statusDesc = statusDesc;
        }

        @Override
        public String toString() {
            return statusDesc;
        }

        public static JourneyStatus convert(String statusDesc){
            for(JourneyStatus status:values()){
                if(status.statusDesc.equals(statusDesc)){
                    return status;
                }
            }
            return EXCEPTION;
        }
    }

    public static class Builder {
        JourneyHistoryItem item = new JourneyHistoryItem();

        public Builder journeyDate(Date date) {
            item.setJourneyDate(date);
            return this;
        }

        public Builder inStationName(String name) {
            item.setInStationName(name);
            return this;
        }

        public Builder outStationName(String name) {
            item.setOutStationName(name);
            return this;
        }

        public Builder status(JourneyStatus status) {
            item.setStatus(status);
            return this;
        }

        public Builder status(String status) {
            if (StringUtils.isBlank(status)) {
                item.setStatus(JourneyStatus.PAYED);
            }
            item.setStatus(JourneyStatus.convert(status));
            return this;
        }

        public Builder inDealTime(String time) {
            item.setInDealTime(time);
            return this;
        }

        public Builder outDealTime(String time) {
            item.setOutDealTime(time);
            return this;
        }

        public JourneyHistoryItem build() {
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
