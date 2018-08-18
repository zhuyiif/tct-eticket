package com.example.eticket.model;

public class JourneyListResponse {
    private String message;
    private int code;
    private JourneyListResult content;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public JourneyListResult getContent() {
        return content;
    }

    public void setContent(JourneyListResult content) {
        this.content = content;
    }
}
