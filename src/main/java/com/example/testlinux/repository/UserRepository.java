package com.example.testlinux.repository;

import com.example.testlinux.domain.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Integer> {
    @Query("SELECT t FROM User t WHERE t.bigemail = :id")
    List<User> getUser(String id);
}
