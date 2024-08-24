package com.example.testlinux.controller;

import com.example.testlinux.domain.User;
import com.example.testlinux.repository.UserRepository;
import com.example.testlinux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class MainController {
    @Autowired
    private UserService userService;

    @Autowired // This means to get the bean called userRepository
    // Which is auto-generated by Spring, we will use it to handle the data
    private UserRepository userRepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewUser (@RequestParam String name, @RequestParam String email) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        User n = new User();
        n.setName(name);
        n.setEmail(email);
        userRepository.save(n);

        List<User> users = userRepository.findAllUsers();
        System.out.println("Saved User List users.size(): " + users.size());

        Optional<User> savedUser = userRepository.userExists(name, email);
        if (savedUser.isPresent()) {
            return "User already exists";
        }
        System.out.println("addNewUser");
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @GetMapping(path="/sqlinjection") // Map ONLY POST Requests
    public @ResponseBody String sqlInjection (@RequestParam String sqlName) {
        List<User> user = userRepository.getUser(sqlName);
        return user.get(0).getName();
    }

    @RequestMapping(value = "/hellonikitin", method = RequestMethod.GET)
    @ResponseBody
    public String helloNikitin() {
        return "Hello, Nikitin!";
    }
}
