package com.example.testlinux.dto;

import lombok.Data;

@Data
public class UserFighterDto {
    private Integer userId;
    private String pass;
    private String telefon;
    private Integer fighterId;
    private String firstName;
    private String lastName;

    // Конструкторы, геттеры и сеттеры
}
