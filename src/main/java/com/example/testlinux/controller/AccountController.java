package com.example.testlinux.controller;

import com.example.testlinux.config.helloworld.MyBean;
import com.example.testlinux.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    @Qualifier("bean2")
    private MyBean myBean;

    // Читаем баланс (может вернуть грязные данные)
    @GetMapping("/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long id) {
        myBean.sayHello();
        BigDecimal balance = accountService.getBalance(id);
        return ResponseEntity.ok(balance);
    }

    // Обновляем баланс (без коммита)
    @PostMapping("/{id}/update-balance")
    public ResponseEntity<String> updateBalance(@PathVariable Long id, @RequestParam BigDecimal newBalance) {
        accountService.updateBalanceWithoutCommit(id, newBalance);
        return ResponseEntity.ok("Balance updated (but not committed yet)!");
    }
}

