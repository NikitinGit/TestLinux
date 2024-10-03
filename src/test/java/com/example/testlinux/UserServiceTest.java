package com.example.testlinux;

import com.example.testlinux.domain.UserNew;
import com.example.testlinux.dto.UserDto;
import com.example.testlinux.repository.UserNewRepository;
import com.example.testlinux.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
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
    private UserNewRepository userNewRepository;

    @Test
    void testGetUsersWithNoUsersInDatabase() {
        //when(userRepository.findAllUsers()).thenReturn(Collections.emptyList());
        ResponseEntity<List<UserDto>> responseEntity = userService.getUsers();
        //assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void addUser() {
        userService.addUser(new UserNew("Nikitin 2","nikitin2.@gmail.com"));
        userService.getUsers();
    }

    @Test
    void testDataLombok() {
        UserNew testEntity = new UserNew("Stepan1","ivanov@gmail.com");
        testEntity.setName("setName");
        Set<UserNew> set = new HashSet<>();

        set.add(testEntity);
        userNewRepository.save(testEntity);

        System.out.println("testEntity.getName(): " + testEntity.getName() +
                " hashCode: " + testEntity.hashCode());
        UserNew userNewTest = set.stream().findFirst().get();
        System.out.println("userTest.getName(): " + userNewTest.getName() +
                " hashCode: " + userNewTest.hashCode());

        Assert.isTrue(set.contains(testEntity), "Entity not found in the set");
    }



}