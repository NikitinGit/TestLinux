package com.example.testlinux;

import com.example.testlinux.controller.ControllerRest;
import com.example.testlinux.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@WebMvcTest(MainController.class)
@Slf4j
@SpringBootTest
class TestLinuxApplicationTests {
    @Autowired
    private ControllerRest controllerRest;

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }

    @Test
    void getUser(){
        log.info("userService.getUserById(1): {}", userService.getUserById(1));
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

    @Test
    public void testGetAllFighters(){
        controllerRest.getFighters();
    }

}
