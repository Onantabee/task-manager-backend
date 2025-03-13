package com.tskmgmnt.rhine.dto;

import java.util.Objects;

public class UserLogReq {
    private final String email;
    private final String password;

    public UserLogReq(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        UserLogReq that = (UserLogReq) o;
        return Objects.equals(email, that.email) && Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, password);
    }

    @Override
    public String toString() {
        return "UserLogReq{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
