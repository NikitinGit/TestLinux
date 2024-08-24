package com.example.testlinux;

import com.example.testlinux.controller.ControllerRest;
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
import org.springframework.stereotype.Controller;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(MainController.class)
@SpringBootTest
class TestLinuxApplicationTests {
    @Autowired
    private ControllerRest controllerRest;

    @Test
    void contextLoads() {
    }


    // @Transactional   @Commit   @Rollback(false)
    @Test
    void testAddNewUserWithExistingNameAndEmail() throws Exception {
        /*String existingName = "Nikitin3";
        String existingEmail = "Igor3@gmail.com";
        when(userRepository.userExists(existingName, existingEmail)).thenReturn(Optional.of(new User()));
        mockMvc.perform(MockMvcRequestBuilders.post("/demo/add")
                        .param("name", existingName)
                        .param("email", existingEmail)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string("User already exists"));*/
    }

    @Test
    public void testGetAllUsers(){
        controllerRest.getUsers();
    }

}
