package com.example.testlinux.dto;

import lombok.Data;

@Data
public class SimpleResponseDto {
    private String status;

    public SimpleResponseDto(String status){
        this.status = status;
    }
}
