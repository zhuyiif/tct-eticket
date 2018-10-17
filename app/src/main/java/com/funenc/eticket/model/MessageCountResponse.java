package com.funenc.eticket.model;

public class MessageCountResponse {
    public static class Result {

        private int messageCount;
        private int notifCount;
        public void setMessageCount(int messageCount) {
            this.messageCount = messageCount;
        }
        public int getMessageCount() {
            return messageCount;
        }

        public void setNotifCount(int notifCount) {
            this.notifCount = notifCount;
        }
        public int getNotifCount() {
            return notifCount;
        }

    }
    private Result content;
    private int code;
    private String message;
    public void setContent(Result result) {
        this.content = result;
    }
    public Result getContent() {
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
