package com.example.testlinux;

import com.example.testlinux.dto.UserDto;
import com.example.testlinux.repository.UserRepository;
import com.example.testlinux.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@SpringBootTest(/*webEnvironment = SpringBootTest.WebEnvironment.MOCK, */classes = TestLinuxApplication.class)
//@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application.properties")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository; // Assuming UserRepository is defined in another class

    @Test
    void testGetUsersWithNoUsersInDatabase() {
        //when(userRepository.findAllUsers()).thenReturn(Collections.emptyList());
        ResponseEntity<List<UserDto>> responseEntity = userService.getUsers();

    }

    @Test
    void testTry(){
        try{
            int data=100/0;
        }catch(Exception e){
            System.out.println("ArithmeticException by nikitin 25; " + e);
        }
    }

}