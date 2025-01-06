package com.example.testlinux.dto;

import com.example.testlinux.domain.Fighter;

public class FighterDto {
    private String fighterName = "hi";
    private String phoneByFighter;

    public FighterDto (Fighter fighter) {
        fighterName = fighter.getFirstName() + " " + fighter.getLastName();
        //phoneByFighter = fighter.getPhoneByFighter();
    }

    public FighterDto(String firstName, String phoneByFighter){
        fighterName = firstName;
        this.phoneByFighter = phoneByFighter;
    }

    public String getFighterName(){
        return fighterName;
    }

    public String getPhoneByFighter(){
        return phoneByFighter;
    }
}
