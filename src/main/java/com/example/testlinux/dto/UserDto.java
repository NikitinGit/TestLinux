package com.example.testlinux.dto;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class UserDto {
    private String userName;

    public UserDto (String userName) {
        this.userName = userName;
    }
}
