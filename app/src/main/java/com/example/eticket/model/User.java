package com.example.eticket.model;

public class User {
    private float id;
    private String name;
    private String gender = null;
    private String phone;
    private String email = null;
    private String avatar;
    private boolean isBlack;
    private float mileage;
    private float score;
    private String createdAt;
    private String updatedAt;


    // Getter Methods
    public float getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public boolean getIsBlack() {
        return isBlack;
    }

    public float getMileage() {
        return mileage;
    }

    public float getScore() {
        return score;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    // Setter Methods

    public void setId( float id ) {
        this.id = id;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setGender( String gender ) {
        this.gender = gender;
    }

    public void setPhone( String phone ) {
        this.phone = phone;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public void setAvatar( String avatar ) {
        this.avatar = avatar;
    }

    public void setIsBlack( boolean isBlack ) {
        this.isBlack = isBlack;
    }

    public void setMileage( float mileage ) {
        this.mileage = mileage;
    }

    public void setScore( float score ) {
        this.score = score;
    }

    public void setCreatedAt( String createdAt ) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt( String updatedAt ) {
        this.updatedAt = updatedAt;
    }
}
