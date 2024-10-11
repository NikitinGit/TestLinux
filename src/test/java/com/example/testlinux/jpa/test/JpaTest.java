package com.example.testlinux.jpa.test;

import com.example.testlinux.domain.UserNew;
import com.example.testlinux.dto.UserDto;
import com.example.testlinux.repository.UserNewRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@DataJpaTest
@AutoConfigureTestDatabase (replace = AutoConfigureTestDatabase.Replace.NONE)
public class JpaTest {
    @Autowired
    private UserNewRepository userNewRepository;

    @BeforeEach
    @Sql("/test-data.sql")
    public void setUp() {
        System.out.println("setUp()");
        for (int n = 0; n < 100; n++){
            String name = "Test #" + n;
            String email = "Emil #" + n;
            userNewRepository.save(new UserNew(name, email));
        }
        // Можно добавить дополнительные настройки, если необходимо
    }

    @Test
    void addUserFromThis() {
        userNewRepository.save(new UserNew("Test Nikitin","test.@gmail.com"));
        getUser();
    }

    @Test
    void getUser(){
        System.out.println("getUser()");
        List<UserNew> userNews = userNewRepository.findAllUsers();
        ArrayList<UserDto> usersDto = new ArrayList<>();
        for(UserNew userNew : userNews){
            log.info("UserService getUsers() User.getName() {}; ", userNew.getName());
            usersDto.add(new UserDto(userNew.getName()));
        }
    }
}
