package com.example.testlinux.dto;

public class UserDto {
    private String userName = "hi";

    public UserDto (String userName) {
        this.userName = userName;
    }
    public String getUserName() {
        return userName;
    }
}
