package com.example.testlinux;

import com.example.testlinux.domain.UserNew;
import com.example.testlinux.dto.UserDto;
import com.example.testlinux.repository.UserNewRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application.properties")
public class TestMysql {
    @Mock
    private UserNewRepository userNewRepository;

    @Autowired
    private DataSource dataSource;

    @Test
    @Sql("/test-data.sql")
    void testMySQLConnection() throws SQLException {
        List<UserNew> userNews = userNewRepository.findAllUsers();
        ArrayList<UserDto> usersDto = new ArrayList<>();
        for(UserNew userNew : userNews){
            System.out.println("UserService getUsers() User.getName(); " + userNew.getName());
            usersDto.add(new UserDto(userNew.getName()));
        }
       /* try (Connection connection = dataSource.getConnection()) {
            // Здесь можно выполнять тестовые запросы к базе данных
            System.out.println("Успешно подключились к MySQL!");
            List<User> users = userRepository.findAllUsers();
            ArrayList<UserDto> usersDto = new ArrayList<>();
            for(User user : users){
                System.out.println("UserService getUsers() User.getName(); " + user.getName());
                usersDto.add(new UserDto(user.getName()));
            }
        }*/
    }
}
