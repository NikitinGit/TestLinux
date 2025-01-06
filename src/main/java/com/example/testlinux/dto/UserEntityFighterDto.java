package com.example.testlinux.dto;

import com.example.testlinux.domain.Fighter;
import lombok.Data;

@Data
public class UserEntityFighterDto {
    private Fighter fighter;

    public UserEntityFighterDto(Fighter fighter) {
        this.fighter = fighter;
    }
}
