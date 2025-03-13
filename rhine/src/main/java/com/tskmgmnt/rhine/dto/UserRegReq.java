package com.tskmgmnt.rhine.dto;

import com.tskmgmnt.rhine.enums.UserRole;

import java.util.Objects;

public class UserRegReq {
    private final String name;
    private final String email;
    private final String pwd;
    private final UserRole userRole;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserRegReq that = (UserRegReq) o;
        return Objects.equals(name, that.name) && Objects.equals(email, that.email) && Objects.equals(pwd, that.pwd) && userRole == that.userRole;
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, pwd, userRole);
    }

    @Override
    public String toString() {
        return "UserRegReq{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", pwd='" + pwd + '\'' +
                ", userRole=" + userRole +
                '}';
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPwd() {
        return pwd;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public UserRegReq(String name, String email, String pwd, UserRole userRole) {
        this.name = name;
        this.email = email;
        this.pwd = pwd;
        this.userRole = userRole;
    }
}
