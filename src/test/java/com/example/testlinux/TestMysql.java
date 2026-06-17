package com.example.testlinux;

import com.example.testlinux.domain.User;
import com.example.testlinux.dto.UserDto;
import com.example.testlinux.repository.UserRepository;
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
    private UserRepository userRepository;

    @Autowired
    private DataSource dataSource;

    @Test
    @Sql("/test-data.sql")
    void testMySQLConnection() throws SQLException {
        List<User> users = userRepository.findAllUsers();
        ArrayList<UserDto> usersDto = new ArrayList<>();
        for(User user : users){
            System.out.println("UserService getUsers() User.getAcstatus(); " + user.getPass());
            usersDto.add(new UserDto(user.getPass()));
        }
       /* try (Connection connection = dataSource.getConnection()) {
            // Здесь можно выполнять тестовые запросы к базе данных
            System.out.println("Успешно подключились к MySQL!");
            List<User> users = userRepository.findAllUsers();
            ArrayList<UserDto> usersDto = new ArrayList<>();
            for(User user : users){
                System.out.println("UserService getUsers() User.getAcstatus(); " + user.getAcstatus());
                usersDto.add(new UserDto(user.getAcstatus()));
            }
        }*/
    }
}
