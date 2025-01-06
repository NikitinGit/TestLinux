package com.example.testlinux.service;

import com.example.testlinux.domain.Event;
import com.example.testlinux.domain.UserNew;
import com.example.testlinux.dto.*;
import com.example.testlinux.repository.EventBidFighterRepository;
import com.example.testlinux.repository.EventRepository;
import com.example.testlinux.repository.FighterRepository;
import com.example.testlinux.repository.UserNewRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserNewRepository userNewRepository;
    @Autowired
    private FighterRepository fighterRepository;
    private final EventBidFighterRepository eventBidFighterRepository;
    private final EventRepository eventRepository;

    public UserService(EventBidFighterRepository eventBidFighter, EventRepository eventRepository) {

        this.eventBidFighterRepository = eventBidFighter;
        this.eventRepository = eventRepository;
    }

    public String addUser(UserNew userNew) {
        userNewRepository.save(userNew);
        return "User added successfully";
    }

    public ResponseEntity<List<UserDto>> getUsers() {
        List<UserNew> userNews = userNewRepository.findAllUsers();
        ArrayList<UserDto> usersDto = new ArrayList<>();
        for (UserNew userNew : userNews) {
            System.out.println("UserService getUsers() User.getAcstatus(); " + userNew.getPass());
            usersDto.add(new UserDto(userNew.getPass()));
        }

        return ResponseEntity.ok(usersDto);
    }

    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> numKey = new HashMap<>();

        for (int i = 0; i < nums.length; i++) {
            int value1 = target - nums[i];

            if (numKey.containsKey(value1)){
                int j = numKey.get(value1);

                return new int[]{i, j};
            }
            numKey.put(nums[i], i);
        }

        return new int[]{};
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
        Event event = eventRepository.findEventByEventId(175).get();
        System.out.println("eventName; " + event.getNameEvent());
        List<FighterDto> fighterDtoList = new ArrayList<>();
        int n = 0;
        fighterDtoList = eventBidFighterRepository.getAllFightersByEventId(175);
        for (FighterDto fighterDto : fighterDtoList) {
            System.out.println("fighterDto.getFirstName(): " + fighterDto.getFighterName() +
                    ", phoneByFighter(): " + fighterDto.getPhoneByFighter());
            if (++n > 5) break;
        }

        return ResponseEntity.ok(fighterDtoList);
    }

    public String getUserById(Integer id) {
        //List<UserFighterDto> userOpt = userNewRepository.findUserById(id);
        //Optional<Event> eventOpt = eventRepository.findEventByEventId(id);

        Optional<UserNew> userOpt = userNewRepository.findUserById(id);
        //UserDto user = userNewRepository.getUserFast(1L).get();
        //Optional<UserEntityDto> userOpt = userNewRepository.getRole(id);
        if (userOpt.isEmpty()) {
            return "empty";
        }
        UserNew user = userOpt.get();

        //UserEntityDto user = optUser.get();
        /*Optional<UserEntityFighterDto> optUser = userNewRepository.getFighter(1L);
        if (!optUser.isPresent()) {
            return ResponseEntity.ok("empty");
        }
        UserEntityFighterDto user = optUser.get();*/

/*        if (user.getJudge() != null){
            name = user.getJudge().getFullName();
            log.info("user.getJudge().getFullName();", user.getJudge().getFullName());
        }*/

        /*if (user.getFighter() != null){
            name = user.getFighter().getFirstName();
            log.info("user.getFighter().getFullName();", name);
        }*/

/*        if (user.getTrainer() != null){
            name = user.getTrainer().getFullname();
            log.info("user.getTrainer().getFullName();", name);
        }*/

        return user.getFighter().getFirstName() + user.getFighter().getLastName();
    }

    public int getSum(int n1, int n2) {
        System.out.println("getSum");
        return n1 + n2;
    }
}
