package com.example.eticket.model;

import java.util.ArrayList;

public class HeadlineListResponse {
    public static class Headline {

        private float id;
        private String title;
        private String cover;
        private String address;
        private String stationId = null;
        private String consume;
        private String category;
        private String telephone;
        private String intro;
        private String time;
        private float categoryId;
        private float viewCount;
        private String url;


        // Getter Methods

        public float getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public String getCover() {
            return cover;
        }

        public String getAddress() {
            return address;
        }

        public String getStationId() {
            return stationId;
        }

        public String getConsume() {
            return consume;
        }

        public String getCategory() {
            return category;
        }

        public String getTelephone() {
            return telephone;
        }

        public String getIntro() {
            return intro;
        }

        public String getTime() {
            return time;
        }

        public float getCategoryId() {
            return categoryId;
        }

        public float getViewCount() {
            return viewCount;
        }

        public String getUrl() {
            return url;
        }

        // Setter Methods

        public void setId(float id) {
            this.id = id;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setCover(String cover) {
            this.cover = cover;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setStationId(String stationId) {
            this.stationId = stationId;
        }

        public void setConsume(String consume) {
            this.consume = consume;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setCategoryId(float categoryId) {
            this.categoryId = categoryId;
        }

        public void setViewCount(float viewCount) {
            this.viewCount = viewCount;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class HeadlineListResult {
        ArrayList<Headline> list = new ArrayList<Headline>();
        private String outstation;
        private float page;
        private float pageSize;
        private float totalRecords;
        private boolean more;


        // Getter Methods

        public String getOutstation() {
            return outstation;
        }

        public float getPage() {
            return page;
        }

        public float getPageSize() {
            return pageSize;
        }

        public float getTotalRecords() {
            return totalRecords;
        }

        public boolean getMore() {
            return more;
        }

        // Setter Methods

        public void setOutstation(String outstation) {
            this.outstation = outstation;
        }

        public void setPage(float page) {
            this.page = page;
        }

        public void setPageSize(float pageSize) {
            this.pageSize = pageSize;
        }

        public void setTotalRecords(float totalRecords) {
            this.totalRecords = totalRecords;
        }

        public void setMore(boolean more) {
            this.more = more;
        }

        public ArrayList<Headline> getList() {
            return list;
        }

        public void setList(ArrayList<Headline> list) {
            this.list = list;
        }
    }

    HeadlineListResult headlineListResult;
    private float code;
    private String message;


    // Getter Methods

    public HeadlineListResult getContent() {
        return headlineListResult;
    }

    public float getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    // Setter Methods

    public void setContent(HeadlineListResult headlineListResult) {
        this.headlineListResult = headlineListResult;
    }

    public void setCode(float code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
