package com.tskmgmnt.rhine.dto;

import com.tskmgmnt.rhine.enums.UserRole;

public class UserRes {  // Renamed to `UserRes` to indicate response DTO
    private String email;
    private String name;
    private UserRole userRole;

    public UserRes(String email, String name, UserRole userRole) {
        this.email = email;
        this.name = name;
        this.userRole = userRole;
    }

    public String getEmail() { return email; }
    public String getName() { return name; }
    public UserRole getUserRole() { return userRole; }
}
