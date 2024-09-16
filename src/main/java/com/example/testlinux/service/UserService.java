package com.example.testlinux.service;

import com.example.testlinux.domain.EventBidFighter;
import com.example.testlinux.domain.Fighter;
import com.example.testlinux.domain.UserNew;
import com.example.testlinux.dto.FighterDto;
import com.example.testlinux.dto.UserDto;
import com.example.testlinux.repository.EventBidFighterRepository;
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
    private final EventBidFighterRepository eventBidFighterRepository;

    public UserService(UserNewRepository userNewRepository, FighterRepository fighterRepository,
                       EventBidFighterRepository eventBidFighter) {
        this.userNewRepository = userNewRepository;
        this.fighterRepository = fighterRepository;
        this.eventBidFighterRepository = eventBidFighter;
    }

    public String addUser(UserNew userNew) {
        userNewRepository.save(userNew);
        return "User added successfully";
    }

    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserNew> userNews = userNewRepository.findAllUsers();
        ArrayList<UserDto> usersDto = new ArrayList<>();
        for (UserNew userNew : userNews) {
            System.out.println("UserService getUsers() User.getName(); " + userNew.getName());
            usersDto.add(new UserDto(userNew.getName()));
        }

        return ResponseEntity.ok(usersDto);
    }

    /*public ResponseEntity <List <FighterDto>> getFighters(){
        int n = 0;
        List<EventBidFighter> bids = eventBidFighterRepository.getFightersFromBids(122);
        System.out.println("UserService getFighters() bids.size(); " + bids.size());
        ArrayList<FighterDto> fighterDto = new ArrayList<>();
        for(EventBidFighter bid : bids){
            *//*Fighter fighter = bid.getFighter();
            System.out.println("fighter.getFirstName(); " + fighter.getFirstName() +
                    ", bid.getLastName(); " + bid.getId());
            fighterDto.add(new FighterDto(fighter.getFirstName()));*//*
            if (++n > 5) break;
        }

        return ResponseEntity.ok(fighterDto);
    }*/

    public ResponseEntity<List<FighterDto>> getFighters() {
        int n = 0;
        List<FighterDto> fighterDtoList = eventBidFighterRepository.getAllFightersByEventId(122);
        for (FighterDto fighterDto : fighterDtoList) {
            System.out.println("fighterDto.getFirstName(): " + fighterDto.getFighterName() +
                    ", phoneByFighter(): " + fighterDto.getPhoneByFighter());
            if (++n > 5) break;
        }

        return ResponseEntity.ok(fighterDtoList);
    }

    public int getSum(int n1, int n2) {
        System.out.println("getSum");
        return n1 + n2;
    }
}
