package com.example.testlinux;

import com.example.testlinux.controller.MainController;
import com.example.testlinux.domain.User;
import com.example.testlinux.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MainController.class)
//@SpringBootTest
//@AutoConfigureMockMvc
class TestLinuxApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

// @Transactional   @Commit   @Rollback(false)
    @Test
    void testAddNewUserWithExistingNameAndEmail() throws Exception {
        String existingName = "Nikitin3";
        String existingEmail = "Igor3@gmail.com";

        when(userRepository.userExists(existingName, existingEmail)).thenReturn(Optional.of(new User()));

        mockMvc.perform(MockMvcRequestBuilders.post("/demo/add")
                        .param("name", existingName)
                        .param("email", existingEmail)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("User already exists"));
    }

    @Test
    void testAddNewUserWith() throws Exception {
        String existingName = "John Doe";
        String existingEmail = "john.doe@example.com";

        // Save a record to the database with the existing name and email
        User existingUser = new User();
        existingUser.setName(existingName);
        existingUser.setEmail(existingEmail);
        userRepository.save(existingUser);

        // Mock the behavior of the userRepository.userExists method
        when(userRepository.userExists(existingName, existingEmail)).thenReturn(Optional.of(existingUser));

        // Perform the POST request to the /demo/add endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/demo/add")
                        .param("name", existingName)
                        .param("email", existingEmail)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("User already exists"));
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
