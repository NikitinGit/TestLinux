package com.example.testlinux.dto;

import com.example.testlinux.domain.Fighter;
import com.example.testlinux.domain.Judge;
import com.example.testlinux.domain.Trainer;
import lombok.Data;

@Data
public class UserEntityDto {
    private Fighter fighter;
    private Judge judge;
    private Trainer trainer;

    public UserEntityDto(Fighter fighter, Judge judge, Trainer trainer) {
        this.fighter = fighter;
        this.judge = judge;
        this.trainer = trainer;
    }

    public UserEntityDto(Fighter fighter) {
        this.fighter = fighter;
    }
}
