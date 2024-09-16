package com.example.testlinux.service;

import com.example.testlinux.domain.Fighter;
import com.example.testlinux.domain.UserNew;
import com.example.testlinux.dto.FighterDto;
import com.example.testlinux.dto.UserDto;
import com.example.testlinux.repository.FighterRepository;
import com.example.testlinux.repository.UserNewRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserNewRepository userNewRepository;
    private final FighterRepository fighterRepository;

    public UserService(UserNewRepository userNewRepository, FighterRepository fighterRepository) {
        this.userNewRepository = userNewRepository;
        this.fighterRepository = fighterRepository;
    }

    public String addUser(UserNew userNew) {
        userNewRepository.save(userNew);
        return "User added successfully";
    }

    public ResponseEntity <List <UserDto>> getUsers(){
        List<UserNew> userNews = userNewRepository.findAllUsers();
        ArrayList<UserDto> usersDto = new ArrayList<>();
        for(UserNew userNew : userNews){
            System.out.println("UserService getUsers() User.getName(); " + userNew.getName());
            usersDto.add(new UserDto(userNew.getName()));
        }

        return ResponseEntity.ok(usersDto);
    }

    public ResponseEntity <List <FighterDto>> getFighters(){
        List<Fighter> fighters = fighterRepository.getAllFightersTest();
        ArrayList<FighterDto> fighterDto = new ArrayList<>();
        for(Fighter fighter : fighters){
            System.out.println("UserService getFighters() fighter.getFirstName(); " + fighter.getFirstName());
            fighterDto.add(new FighterDto(fighter.getFirstName()));
            break;
        }

        return ResponseEntity.ok(fighterDto);
    }

    public int getSum(int n1, int n2) {
        System.out.println("getSum");
        return n1 + n2;
    }
}
