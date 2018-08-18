package com.example.eticket.model;

import java.util.List;

public class JourneyListResult {
    private List<Journey> list;
    private int totalRecords;
    private int page;
    private int pageSize;
    private boolean more;

    public List<Journey> getList() {
        return list;
    }

    public void setList(List<Journey> list) {
        this.list = list;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public boolean isMore() {
        return more;
    }

    public void setMore(boolean more) {
        this.more = more;
    }
}
