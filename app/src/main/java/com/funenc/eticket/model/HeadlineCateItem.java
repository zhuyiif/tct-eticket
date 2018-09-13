package com.funenc.eticket.model;

public class HeadlineCateItem {
    private int id;
    private String title;
    private String createdAt;
    private String updatedAt;


    // Getter Methods
    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    // Setter Methods

    public void setId( int id ) {
        this.id = id;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public void setCreatedAt( String createdAt ) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt( String updatedAt ) {
        this.updatedAt = updatedAt;
    }
}
