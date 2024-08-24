package com.example.testlinux.unit.test;

import com.example.testlinux.domain.User;
import com.example.testlinux.dto.UserDto;
import com.example.testlinux.repository.UserRepository;
import com.example.testlinux.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
//@PropertySource("classpath:application.properties") //not working
@TestPropertySource(locations = "classpath:application.properties")
public class UnitTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void sum(){
        assertEquals(8, userService.getSum(5, 3));
        userService.getUsers();
    }

    @Test
    void getUser(){
        List<User> users = userRepository.findAllUsers();
        ArrayList<UserDto> usersDto = new ArrayList<>();
        for(User user : users){
            System.out.println("UserService getUsers() User.getName(); " + user.getName());
            usersDto.add(new UserDto(user.getName()));
        }
    }
}
