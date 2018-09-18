package com.funenc.eticket.model;

public class UserInfoResponse {
    public static class UserInfo {

        private User user;
        public void setUser(User user) {
            this.user = user;
        }
        public User getUser() {
            return user;
        }

    }
    private UserInfo content;
    private int code;
    private String message;
    public void setContent(UserInfo userInfo) {
        this.content = userInfo;
    }
    public UserInfo getContent() {
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
