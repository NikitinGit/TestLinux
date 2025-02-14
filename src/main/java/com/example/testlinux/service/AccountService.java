package com.example.testlinux.service;

import com.example.testlinux.domain.Account;
import com.example.testlinux.repository.AccountRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Читаем баланс с уровнем изоляции READ UNCOMMITTED (грязное чтение)
     */
    @Transactional(isolation = Isolation.READ_UNCOMMITTED)
    public BigDecimal getBalance(Long accountId) {
        Account account = entityManager.find(Account.class, accountId);
        return account != null ? account.getBalance() : BigDecimal.ZERO;
    }

    /**
     * Обновляем баланс без коммита
     */
    @Transactional
    public void updateBalanceWithoutCommit(Long accountId, BigDecimal newBalance) {
        Account account = entityManager.find(Account.class, accountId);
        if (account != null) {
            account.setBalance(newBalance);
            entityManager.persist(account);
        }
    }
}

