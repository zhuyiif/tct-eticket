package com.funenc.eticket.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by tingken.com on 2018-10-30.
 */
public class OrderListResponse {
    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class InRecord implements Serializable {

        private int id;
        private Date tradeTime;
        private int tradeType;
        private String lineNo;
        private String stationNo;
        private String deviceNo;
        private String deviceType;
        private String deviceSerialNo;
        private String deviceStatus;
        private String qrcode;
        private String devieOrderNo;
        private Date scanTime;
        private int userId;
        private String userCardNo;
        private String userAccountType;
        private String payAccountNo;
        private String bomStationNo;
        private String bomTradeTime;
        private String bomEventNo;
        private boolean pairStatus;
        private boolean deleted;
        private Date createdAt;
        private Date updatedAt;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setTradeTime(Date tradeTime) {
            this.tradeTime = tradeTime;
        }
        public Date getTradeTime() {
            return tradeTime;
        }

        public void setTradeType(int tradeType) {
            this.tradeType = tradeType;
        }
        public int getTradeType() {
            return tradeType;
        }

        public void setLineNo(String lineNo) {
            this.lineNo = lineNo;
        }
        public String getLineNo() {
            return lineNo;
        }

        public void setStationNo(String stationNo) {
            this.stationNo = stationNo;
        }
        public String getStationNo() {
            return stationNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }
        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }
        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceSerialNo(String deviceSerialNo) {
            this.deviceSerialNo = deviceSerialNo;
        }
        public String getDeviceSerialNo() {
            return deviceSerialNo;
        }

        public void setDeviceStatus(String deviceStatus) {
            this.deviceStatus = deviceStatus;
        }
        public String getDeviceStatus() {
            return deviceStatus;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }
        public String getQrcode() {
            return qrcode;
        }

        public void setDevieOrderNo(String devieOrderNo) {
            this.devieOrderNo = devieOrderNo;
        }
        public String getDevieOrderNo() {
            return devieOrderNo;
        }

        public void setScanTime(Date scanTime) {
            this.scanTime = scanTime;
        }
        public Date getScanTime() {
            return scanTime;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
        public int getUserId() {
            return userId;
        }

        public void setUserCardNo(String userCardNo) {
            this.userCardNo = userCardNo;
        }
        public String getUserCardNo() {
            return userCardNo;
        }

        public void setUserAccountType(String userAccountType) {
            this.userAccountType = userAccountType;
        }
        public String getUserAccountType() {
            return userAccountType;
        }

        public void setPayAccountNo(String payAccountNo) {
            this.payAccountNo = payAccountNo;
        }
        public String getPayAccountNo() {
            return payAccountNo;
        }

        public void setBomStationNo(String bomStationNo) {
            this.bomStationNo = bomStationNo;
        }
        public String getBomStationNo() {
            return bomStationNo;
        }

        public void setBomTradeTime(String bomTradeTime) {
            this.bomTradeTime = bomTradeTime;
        }
        public String getBomTradeTime() {
            return bomTradeTime;
        }

        public void setBomEventNo(String bomEventNo) {
            this.bomEventNo = bomEventNo;
        }
        public String getBomEventNo() {
            return bomEventNo;
        }

        public void setPairStatus(boolean pairStatus) {
            this.pairStatus = pairStatus;
        }
        public boolean getPairStatus() {
            return pairStatus;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }
        public boolean getDeleted() {
            return deleted;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }
        public Date getCreatedAt() {
            return createdAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }
        public Date getUpdatedAt() {
            return updatedAt;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class OutRecord implements Serializable {

        private int id;
        private Date tradeTime;
        private int tradeType;
        private String lineNo;
        private String stationNo;
        private String deviceNo;
        private String deviceType;
        private String deviceSerialNo;
        private String deviceStatus;
        private String qrcode;
        private String devieOrderNo;
        private Date scanTime;
        private int userId;
        private String userCardNo;
        private String userAccountType;
        private String payAccountNo;
        private String bomStationNo;
        private Date bomTradeTime;
        private String bomEventNo;
        private boolean pairStatus;
        private boolean deleted;
        private Date createdAt;
        private Date updatedAt;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setTradeTime(Date tradeTime) {
            this.tradeTime = tradeTime;
        }
        public Date getTradeTime() {
            return tradeTime;
        }

        public void setTradeType(int tradeType) {
            this.tradeType = tradeType;
        }
        public int getTradeType() {
            return tradeType;
        }

        public void setLineNo(String lineNo) {
            this.lineNo = lineNo;
        }
        public String getLineNo() {
            return lineNo;
        }

        public void setStationNo(String stationNo) {
            this.stationNo = stationNo;
        }
        public String getStationNo() {
            return stationNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }
        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }
        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceSerialNo(String deviceSerialNo) {
            this.deviceSerialNo = deviceSerialNo;
        }
        public String getDeviceSerialNo() {
            return deviceSerialNo;
        }

        public void setDeviceStatus(String deviceStatus) {
            this.deviceStatus = deviceStatus;
        }
        public String getDeviceStatus() {
            return deviceStatus;
        }

        public void setQrcode(String qrcode) {
            this.qrcode = qrcode;
        }
        public String getQrcode() {
            return qrcode;
        }

        public void setDevieOrderNo(String devieOrderNo) {
            this.devieOrderNo = devieOrderNo;
        }
        public String getDevieOrderNo() {
            return devieOrderNo;
        }

        public void setScanTime(Date scanTime) {
            this.scanTime = scanTime;
        }
        public Date getScanTime() {
            return scanTime;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
        public int getUserId() {
            return userId;
        }

        public void setUserCardNo(String userCardNo) {
            this.userCardNo = userCardNo;
        }
        public String getUserCardNo() {
            return userCardNo;
        }

        public void setUserAccountType(String userAccountType) {
            this.userAccountType = userAccountType;
        }
        public String getUserAccountType() {
            return userAccountType;
        }

        public void setPayAccountNo(String payAccountNo) {
            this.payAccountNo = payAccountNo;
        }
        public String getPayAccountNo() {
            return payAccountNo;
        }

        public void setBomStationNo(String bomStationNo) {
            this.bomStationNo = bomStationNo;
        }
        public String getBomStationNo() {
            return bomStationNo;
        }

        public void setBomTradeTime(Date bomTradeTime) {
            this.bomTradeTime = bomTradeTime;
        }
        public Date getBomTradeTime() {
            return bomTradeTime;
        }

        public void setBomEventNo(String bomEventNo) {
            this.bomEventNo = bomEventNo;
        }
        public String getBomEventNo() {
            return bomEventNo;
        }

        public void setPairStatus(boolean pairStatus) {
            this.pairStatus = pairStatus;
        }
        public boolean getPairStatus() {
            return pairStatus;
        }

        public void setDeleted(boolean deleted) {
            this.deleted = deleted;
        }
        public boolean getDeleted() {
            return deleted;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }
        public Date getCreatedAt() {
            return createdAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }
        public Date getUpdatedAt() {
            return updatedAt;
        }

    }

    @JsonIgnoreProperties(ignoreUnknown=true)
    public static class Order implements Serializable {

        private int id;
        private int userId;
        private int inRecordId;
        private String outRecordId;
        private String status;
        private int deductFee;
        private Date createdAt;
        private Date updatedAt;

        private String userCardNo;
        private String userAccountType;
        private String payAccountNo;
        private Date inTradeTime;
        private float inTradeType;
        private String inLineNo;
        private String inStationNo;
        private String inDeviceNo;
        private String inDeviceType;
        private String inDeviceSerialNo;
        private String inDeviceStatus;
        private String inQrcode;
        private String inDevieOrderNo;
        private String inScanTime;
        private String inBomStationNo = null;
        private String inBomTradeTime = null;
        private String inBomEventNo = null;
        private Date outTradeTime;
        private float outTradeType;
        private String outLineNo;
        private String outStationNo;
        private String outDeviceNo;
        private String outDeviceType;
        private String outDeviceSerialNo;
        private String outDeviceStatus;
        private String outQrcode;
        private String outDevieOrderNo;
        private String outScanTime;
        private String outBomStationNo = null;
        private String outBomTradeTime = null;
        private String outBomEventNo = null;
        private float deleted;
        private String inStationName;
        private String outStationName;
        private InRecord inRecord;
        private OutRecord outRecord;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
        public int getUserId() {
            return userId;
        }

        public void setInRecordId(int inRecordId) {
            this.inRecordId = inRecordId;
        }
        public int getInRecordId() {
            return inRecordId;
        }

        public void setOutRecordId(String outRecordId) {
            this.outRecordId = outRecordId;
        }
        public String getOutRecordId() {
            return outRecordId;
        }

        public void setStatus(String status) {
            this.status = status;
        }
        public String getStatus() {
            return status;
        }

        public void setDeductFee(int deductFee) {
            this.deductFee = deductFee;
        }
        public int getDeductFee() {
            return deductFee;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt = createdAt;
        }
        public Date getCreatedAt() {
            return createdAt;
        }

        public void setUpdatedAt(Date updatedAt) {
            this.updatedAt = updatedAt;
        }
        public Date getUpdatedAt() {
            return updatedAt;
        }

        public void setInRecord(InRecord inRecord) {
            this.inRecord = inRecord;
        }
        public InRecord getInRecord() {
            return inRecord;
        }

        public void setOutRecord(OutRecord outRecord) {
            this.outRecord = outRecord;
        }
        public OutRecord getOutRecord() {
            return outRecord;
        }


        public String getUserCardNo() {
            return userCardNo;
        }

        public String getUserAccountType() {
            return userAccountType;
        }

        public String getPayAccountNo() {
            return payAccountNo;
        }

        public Date getInTradeTime() {
            return inTradeTime;
        }

        public float getInTradeType() {
            return inTradeType;
        }

        public String getInLineNo() {
            return inLineNo;
        }

        public String getInStationNo() {
            return inStationNo;
        }

        public String getInDeviceNo() {
            return inDeviceNo;
        }

        public String getInDeviceType() {
            return inDeviceType;
        }

        public String getInDeviceSerialNo() {
            return inDeviceSerialNo;
        }

        public String getInDeviceStatus() {
            return inDeviceStatus;
        }

        public String getInQrcode() {
            return inQrcode;
        }

        public String getInDevieOrderNo() {
            return inDevieOrderNo;
        }

        public String getInScanTime() {
            return inScanTime;
        }

        public String getInBomStationNo() {
            return inBomStationNo;
        }

        public String getInBomTradeTime() {
            return inBomTradeTime;
        }

        public String getInBomEventNo() {
            return inBomEventNo;
        }

        public Date getOutTradeTime() {
            return outTradeTime;
        }

        public float getOutTradeType() {
            return outTradeType;
        }

        public String getOutLineNo() {
            return outLineNo;
        }

        public String getOutStationNo() {
            return outStationNo;
        }

        public String getOutDeviceNo() {
            return outDeviceNo;
        }

        public String getOutDeviceType() {
            return outDeviceType;
        }

        public String getOutDeviceSerialNo() {
            return outDeviceSerialNo;
        }

        public String getOutDeviceStatus() {
            return outDeviceStatus;
        }

        public String getOutQrcode() {
            return outQrcode;
        }

        public String getOutDevieOrderNo() {
            return outDevieOrderNo;
        }

        public String getOutScanTime() {
            return outScanTime;
        }

        public String getOutBomStationNo() {
            return outBomStationNo;
        }

        public String getOutBomTradeTime() {
            return outBomTradeTime;
        }

        public String getOutBomEventNo() {
            return outBomEventNo;
        }

        public float getDeleted() {
            return deleted;
        }

        public String getInStationName() {
            return inStationName;
        }

        public String getOutStationName() {
            return outStationName;
        }

        public void setUserCardNo( String userCardNo ) {
            this.userCardNo = userCardNo;
        }

        public void setUserAccountType( String userAccountType ) {
            this.userAccountType = userAccountType;
        }

        public void setPayAccountNo( String payAccountNo ) {
            this.payAccountNo = payAccountNo;
        }

        public void setInTradeTime( Date inTradeTime ) {
            this.inTradeTime = inTradeTime;
        }

        public void setInTradeType( float inTradeType ) {
            this.inTradeType = inTradeType;
        }

        public void setInLineNo( String inLineNo ) {
            this.inLineNo = inLineNo;
        }

        public void setInStationNo( String inStationNo ) {
            this.inStationNo = inStationNo;
        }

        public void setInDeviceNo( String inDeviceNo ) {
            this.inDeviceNo = inDeviceNo;
        }

        public void setInDeviceType( String inDeviceType ) {
            this.inDeviceType = inDeviceType;
        }

        public void setInDeviceSerialNo( String inDeviceSerialNo ) {
            this.inDeviceSerialNo = inDeviceSerialNo;
        }

        public void setInDeviceStatus( String inDeviceStatus ) {
            this.inDeviceStatus = inDeviceStatus;
        }

        public void setInQrcode( String inQrcode ) {
            this.inQrcode = inQrcode;
        }

        public void setInDevieOrderNo( String inDevieOrderNo ) {
            this.inDevieOrderNo = inDevieOrderNo;
        }

        public void setInScanTime( String inScanTime ) {
            this.inScanTime = inScanTime;
        }

        public void setInBomStationNo( String inBomStationNo ) {
            this.inBomStationNo = inBomStationNo;
        }

        public void setInBomTradeTime( String inBomTradeTime ) {
            this.inBomTradeTime = inBomTradeTime;
        }

        public void setInBomEventNo( String inBomEventNo ) {
            this.inBomEventNo = inBomEventNo;
        }

        public void setOutTradeTime( Date outTradeTime ) {
            this.outTradeTime = outTradeTime;
        }

        public void setOutTradeType( float outTradeType ) {
            this.outTradeType = outTradeType;
        }

        public void setOutLineNo( String outLineNo ) {
            this.outLineNo = outLineNo;
        }

        public void setOutStationNo( String outStationNo ) {
            this.outStationNo = outStationNo;
        }

        public void setOutDeviceNo( String outDeviceNo ) {
            this.outDeviceNo = outDeviceNo;
        }

        public void setOutDeviceType( String outDeviceType ) {
            this.outDeviceType = outDeviceType;
        }

        public void setOutDeviceSerialNo( String outDeviceSerialNo ) {
            this.outDeviceSerialNo = outDeviceSerialNo;
        }

        public void setOutDeviceStatus( String outDeviceStatus ) {
            this.outDeviceStatus = outDeviceStatus;
        }

        public void setOutQrcode( String outQrcode ) {
            this.outQrcode = outQrcode;
        }

        public void setOutDevieOrderNo( String outDevieOrderNo ) {
            this.outDevieOrderNo = outDevieOrderNo;
        }

        public void setOutScanTime( String outScanTime ) {
            this.outScanTime = outScanTime;
        }

        public void setOutBomStationNo( String outBomStationNo ) {
            this.outBomStationNo = outBomStationNo;
        }

        public void setOutBomTradeTime( String outBomTradeTime ) {
            this.outBomTradeTime = outBomTradeTime;
        }

        public void setOutBomEventNo( String outBomEventNo ) {
            this.outBomEventNo = outBomEventNo;
        }

        public void setDeleted( float deleted ) {
            this.deleted = deleted;
        }

        public void setInStationName( String inStationName ) {
            this.inStationName = inStationName;
        }

        public void setOutStationName( String outStationName ) {
            this.outStationName = outStationName;
        }
    }

    public static class OrderListResult {

        private List<Order> list;
        private int page;
        private int pageSize;
        private int totalRecords;
        private boolean more;
        public void setList(List<Order> list) {
            this.list = list;
        }
        public List<Order> getList() {
            return list;
        }

        public void setPage(int page) {
            this.page = page;
        }
        public int getPage() {
            return page;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }
        public int getPageSize() {
            return pageSize;
        }

        public void setTotalRecords(int totalRecords) {
            this.totalRecords = totalRecords;
        }
        public int getTotalRecords() {
            return totalRecords;
        }

        public void setMore(boolean more) {
            this.more = more;
        }
        public boolean getMore() {
            return more;
        }

    }

    private OrderListResult content;
    private int code;
    private String message;
    public void setContent(OrderListResult content) {
        this.content = content;
    }
    public OrderListResult getContent() {
        return content;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

}
