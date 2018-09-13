package com.funenc.eticket.model;

import java.util.ArrayList;

public class FelicityListResponse {
    public static class Felicity{
        private int id;
        private String title;
        private String cover;
        private String intro;
        private String url;
        private String color;
        private String createdAt;
        private String updatedAt;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getCover() {
            return cover;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
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

    public static class FelicityListResult {
        private ArrayList<Felicity> list = new ArrayList<Felicity>();

        public ArrayList<Felicity> getList() {
            return list;
        }

        public void setList(ArrayList<Felicity> list) {
            this.list = list;
        }
    }
    private String message;
    private int code;
    private FelicityListResult content;

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

    public FelicityListResult getContent() {
        return content;
    }

    public void setContent(FelicityListResult content) {
        this.content = content;
    }
}
