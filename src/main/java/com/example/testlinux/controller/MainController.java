package com.example.testlinux.controller;

import com.example.testlinux.domain.UserNew;
import com.example.testlinux.repository.UserNewRepository;
import com.example.testlinux.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
    private UserNewRepository userNewRepository;

    @PostMapping(path="/add")
    public @ResponseBody String addNewUser (@RequestParam String name, @RequestParam String email) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        UserNew n = new UserNew();
        n.setName(name);
        n.setEmail(email);
        userNewRepository.save(n);

        List<UserNew> userNews = userNewRepository.findAllUsers();
        System.out.println("Saved User List users.size(): " + userNews.size());

        Optional<UserNew> savedUser = userNewRepository.userExists(name, email);
        if (savedUser.isPresent()) {
            return "User already exists";
        }
        System.out.println("addNewUser");
        return "Saved";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<UserNew> getAllUsers() {
        // This returns a JSON or XML with the users
        return userNewRepository.findAll();
    }

    @GetMapping(path="/sqlinjection") // Map ONLY POST Requests
    public @ResponseBody String sqlInjection (@RequestParam String sqlName) {
        List<UserNew> userNew = userNewRepository.getUser(sqlName);
        return userNew.get(0).getName();
    }

    @RequestMapping(value = "/hellonikitin", method = RequestMethod.GET)
    @ResponseBody
    public String helloNikitin() {
        return "Hello, Nikitin!";
    }
}
