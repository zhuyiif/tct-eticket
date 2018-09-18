package com.funenc.eticket.model;

public class QiniuTokenResponse {
    public static class QiniuTokenResult{
        private String token;
        public void setToken(String token) {
            this.token = token;
        }
        public String getToken() {
            return token;
        }
    }
    private QiniuTokenResult content;
    private int code;
    private String message;
    public void setContent(QiniuTokenResult content) {
        this.content = content;
    }
    public QiniuTokenResult getContent() {
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
