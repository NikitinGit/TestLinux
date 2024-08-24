package com.example.testlinux.service;

import com.example.testlinux.domain.User;
import com.example.testlinux.dto.UserDto;
import com.example.testlinux.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String addUser(User user) {
        userRepository.save(user);
        return "User added successfully";
    }

    public ResponseEntity <List <UserDto>> getUsers(){
        List<User> users = userRepository.findAllUsers();
        ArrayList<UserDto> usersDto = new ArrayList<>();
        for(User user : users){
            System.out.println("UserService getUsers() User.getName(); " + user.getName());
            usersDto.add(new UserDto(user.getName()));
        }

        return ResponseEntity.ok(usersDto);
    }

    public int getSum(int n1, int n2) {
        System.out.println("getSum");
        return n1 + n2;
    }
}
