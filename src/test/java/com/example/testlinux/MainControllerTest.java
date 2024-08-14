package com.example.testlinux;

import com.example.testlinux.controller.MainController;
import com.example.testlinux.domain.User;
import com.example.testlinux.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MainController.class)
public class MainControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void testAddNewUser() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/demo/add")
                        .param("name", "John Doe 2")
                        .param("email", "john.doe@example2.com")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("Saved"));
    }

    @Test
    public void testGetAllUsers() throws Exception {
        User user1 = new User();
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");

        User user2 = new User();
        user2.setName("Jane Smith");
        user2.setEmail("jane.smith@example.com");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(MockMvcRequestBuilders.get("/demo/all"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"},{\"name\":\"Jane Smith\",\"email\":\"jane.smith@example.com\"}]"));
    }

    @Test
    public void testSqlInjection() throws Exception {
        User user = new User();
        user.setName("John Doe");
        user.setEmail("john.doe@example.com");

        when(userRepository.getUser("John Doe")).thenReturn(Arrays.asList(user));

        mockMvc.perform(MockMvcRequestBuilders.get("/demo/sqlinjection")
                        .param("sqlName", "John Doe"))
                .andExpect(status().isOk())
                .andExpect(content().string("John Doe"));
    }
}
