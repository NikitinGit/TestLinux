package com.example.testlinux.jpa.test;

import com.example.testlinux.domain.User;
import com.example.testlinux.dto.UserDto;
import com.example.testlinux.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase (replace = AutoConfigureTestDatabase.Replace.NONE)
public class JpaTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    //@Sql("/test-data.sql")
    public void setUp() {
        System.out.println("setUp()");
        for (int n = 0; n < 100; n++){
            String name = "Test #" + n;
            String email = "Emil #" + n;
            userRepository.save(new User(name, email));
        }
        // Можно добавить дополнительные настройки, если необходимо
    }

    @Test
    void addUserFromThis() {
        userRepository.save(new User("Test Nikitin","test.@gmail.com"));
        getUser();
    }

    @Test
    void getUser(){
        System.out.println("getUser()");
        List<User> users = userRepository.findAllUsers();
        ArrayList<UserDto> usersDto = new ArrayList<>();
        for(User user : users){
            log.info("UserService getUsers() User.getAcstatus() {}; ", user.getPass());
            usersDto.add(new UserDto(user.getPass()));
        }
    }
}
