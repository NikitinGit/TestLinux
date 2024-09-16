package com.example.testlinux.unit.test;

import com.example.testlinux.domain.UserNew;
import com.example.testlinux.dto.UserDto;
import com.example.testlinux.repository.UserNewRepository;
import com.example.testlinux.service.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
//@PropertySource("classpath:application.properties") //not working
@TestPropertySource(locations = "classpath:application.properties")
public class UnitTest {

    @Mock
    private UserNewRepository userNewRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void sum(){
        assertEquals(8, userService.getSum(5, 3));
        userService.getUsers();
    }

    @Test
    void getUser(){
        List<UserNew> userNews = userNewRepository.findAllUsers();
        ArrayList<UserDto> usersDto = new ArrayList<>();
        for(UserNew userNew : userNews){
            System.out.println("UserService getUsers() User.getName(); " + userNew.getName());
            usersDto.add(new UserDto(userNew.getName()));
        }
    }
}
