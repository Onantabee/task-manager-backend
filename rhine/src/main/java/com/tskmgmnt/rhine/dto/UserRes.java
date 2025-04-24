package com.tskmgmnt.rhine.dto;

import com.tskmgmnt.rhine.enums.UserRole;

public class UserRes {  // Renamed to `UserRes` to indicate response DTO
    private String email;
    private String name;
    private UserRole userRole;
    private String pwd;

    public UserRes(String email, String name, UserRole userRole) {
        this.email = email;
        this.name = name;
        this.userRole = userRole;
    }

    public UserRes(String pwd) {
        this.pwd = pwd;
    }

    public UserRes() {

    }

    public String getEmail() { return email; }
    public String getName() { return name; }
    public UserRole getUserRole() { return userRole; }

    public String getPwd() {
        return pwd;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
