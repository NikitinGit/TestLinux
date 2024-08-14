package com.example.testlinux.repository;

import com.example.testlinux.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT t FROM User t")
    List<User> findAllUsers();

    @Query("SELECT t FROM User t WHERE t.bigemail = :id")
    List<User> getUser(String id);

    @Query("SELECT t FROM User t WHERE t.bigname = :name AND t.bigemail = :email")
    Optional<User> userExists(String name, String email);
}

