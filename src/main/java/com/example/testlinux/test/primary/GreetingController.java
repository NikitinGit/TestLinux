package com.example.testlinux.test.primary;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/greet")
public class GreetingController {

    private final GreetingService defaultService;
    private final GreetingService russianService;

    public GreetingController(
            GreetingService defaultService, // внедряется @Primary — English
            @Qualifier("englishGreetingService") GreetingService russianService // внедряется по имени
    ) {
        this.defaultService = defaultService;
        this.russianService = russianService;
    }

    @GetMapping("/default")
    public String defaultGreeting(@RequestParam String name) {
        return defaultService.greet(name);
    }

    @GetMapping("/ru")
    public String russianGreeting(@RequestParam String name) {
        return russianService.greet(name);
    }
}
