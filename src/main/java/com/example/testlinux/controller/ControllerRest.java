package com.example.testlinux.controller;


import com.example.testlinux.dto.FighterDto;
import com.example.testlinux.dto.UserDto;
import com.example.testlinux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demo")
public class ControllerRest {
    // TODO
    private final UserService userService;

    public ControllerRest(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity <List<UserDto>> getUsers() {
        System.out.println("getUsers");
        return userService.getUsers();
    }

    @GetMapping("/fighters")
    public ResponseEntity <List<FighterDto>> getFighters() {
        System.out.println("getFighters");
        return userService.getFighters();
    }

    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "myName", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }

}
