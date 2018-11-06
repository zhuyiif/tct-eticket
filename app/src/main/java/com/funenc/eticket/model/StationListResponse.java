package com.funenc.eticket.model;

import java.util.List;

/**
 * Created by tingken.com on 2018-10-30.
 */
public class StationListResponse {
    public static class Station {

        private String cStationName;
        private String eStationName;
        private String lineNo;
        private String memo;
        private String stationNo;
        private String transferFlag;
        private List<String> nearNos;
        public void setcStationName(String cStationName) {
            this.cStationName = cStationName;
        }
        public String getcStationName() {
            return cStationName;
        }

        public void seteStationName(String eStationName) {
            this.eStationName = eStationName;
        }
        public String geteStationName() {
            return eStationName;
        }

        public void setLineNo(String lineNo) {
            this.lineNo = lineNo;
        }
        public String getLineNo() {
            return lineNo;
        }

        public void setMemo(String memo) {
            this.memo = memo;
        }
        public String getMemo() {
            return memo;
        }

        public void setStationNo(String stationNo) {
            this.stationNo = stationNo;
        }
        public String getStationNo() {
            return stationNo;
        }

        public void setTransferFlag(String transferFlag) {
            this.transferFlag = transferFlag;
        }
        public String getTransferFlag() {
            return transferFlag;
        }

        public void setNearNos(List<String> nearNos) {
            this.nearNos = nearNos;
        }
        public List<String> getNearNos() {
            return nearNos;
        }

    }

    public static class StationListResult {

        private List<Station> list;
        public void setList(List<Station> list) {
            this.list = list;
        }
        public List<Station> getList() {
            return list;
        }

    }

    private StationListResult content;
    private int code;
    private String message;
    public void setContent(StationListResult content) {
        this.content = content;
    }
    public StationListResult getContent() {
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
