package com.example.testlinux;

import com.example.testlinux.controller.ControllerRest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @Test
    public void testGetAllFighters(){
        controllerRest.getFighters();
    }

}
