package com.example.testlinux.repository;

import com.example.testlinux.domain.UserNew;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface UserNewRepository extends CrudRepository<UserNew, Integer> {
    @Query("SELECT t FROM UserNew t")
    List<UserNew> findAllUsers();

    @Query("SELECT t FROM UserNew t WHERE t.email = :id")
    List<UserNew> getUser(String id);

    @Query("SELECT t FROM UserNew t WHERE t.name = :name AND t.email = :email")
    Optional<UserNew> userExists(String name, String email);
}

