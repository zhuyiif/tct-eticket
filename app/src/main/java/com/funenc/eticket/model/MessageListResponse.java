package com.funenc.eticket.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public class MessageListResponse {
    public static class Message {

        private int id;
        private String title;
        private String content;
        private boolean isRead;
        private boolean isPush;
        private boolean isMessage;
        private String url;
        private String cover;
        private int userId;
        private Date createdAt;
        private Date updatedAt;
        @JsonProperty("UserId")
        private int userid;
        public void setId(int id) {
            this.id = id;
        }
        public int getId() {
            return id;
        }

        public void setTitle(String title) {
            this.title = title;
        }
        public String getTitle() {
            return title;
        }

        public void setContent(String content) {
            this.content = content;
        }
        public String getContent() {
            return content;
        }

        public void setIsRead(boolean read) {
            this.isRead = read;
        }
        public boolean getIsRead() {
            return isRead;
        }

        public void setIsPush(boolean push) {
            this.isPush = push;
        }
        public boolean getIsPush() {
            return isPush;
        }

        public void setIsMessage(boolean message) {
            this.isMessage = message;
        }
        public boolean getIsMessage() {
            return isMessage;
        }

        public void setUrl(String url) {
            this.url = url;
        }
        public String getUrl() {
            return url;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }
        public String getCover() {
            return cover;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
        public int getUserId() {
            return userId;
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

        public void setUserid(int userid) {
            this.userId = userid;
        }
        public int getUserid() {
            return userId;
        }

    }
    public static class MessageListResult {

        private List<Message> messageList;
        private int page;
        private int pageSize;
        private int totalRecords;
        private boolean more;
        public void setList(List<Message> messageList) {
            this.messageList = messageList;
        }
        public List<Message> getList() {
            return messageList;
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
    private MessageListResult messageListResult;
    private int code;
    private String message;
    public void setContent(MessageListResult messageListResult) {
        this.messageListResult = messageListResult;
    }
    public MessageListResult getContent() {
        return messageListResult;
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
