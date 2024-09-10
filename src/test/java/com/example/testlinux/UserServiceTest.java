package com.example.testlinux;

import com.example.testlinux.domain.User;
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
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

@SpringBootTest(/*webEnvironment = SpringBootTest.WebEnvironment.MOCK, */classes = TestLinuxApplication.class)
//@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application.properties")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testGetUsersWithNoUsersInDatabase() {
        //when(userRepository.findAllUsers()).thenReturn(Collections.emptyList());
        ResponseEntity<List<UserDto>> responseEntity = userService.getUsers();
        //assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void addUser() {
        userService.addUser(new User("Nikitin","nikitin.@gmail.com"));
        userService.getUsers();
    }

    @Test
    void testDataLombok() {
        User testEntity = new User("Stepan1","ivanov@gmail.com");
        testEntity.setName("setName");
        Set<User> set = new HashSet<>();

        set.add(testEntity);
        userRepository.save(testEntity);

        System.out.println("testEntity.getName(): " + testEntity.getName() +
                " hashCode: " + testEntity.hashCode());
        User userTest = set.stream().findFirst().get();
        System.out.println("userTest.getName(): " + userTest.getName() +
                " hashCode: " + userTest.hashCode());

        Assert.isTrue(set.contains(testEntity), "Entity not found in the set");
    }



}