package com.tskmgmnt.rhine.dto;

public class MarkAsReadDto {
    private String userEmail;


    public MarkAsReadDto() {
    }

    public MarkAsReadDto(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}