package com.example.testlinux.repository;

import com.example.testlinux.domain.UserNew;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.testlinux.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserNewRepository extends CrudRepository<UserNew, Integer> {
    @Query("SELECT t FROM UserNew t")
    List<UserNew> findAllUsers();

    Optional<UserNew> findUserById(Integer login);

    @Query("SELECT new com.example.testlinux.dto.UserDto(t.pass) FROM UserNew t WHERE t.id = :id")
    Optional<UserDto> getUserFast(Long id);

    @Query("SELECT t FROM UserNew t WHERE t.id = :id")
    List<UserNew> getUser(String id);

    @Query("SELECT t FROM UserNew t WHERE t.pass = :acstatus AND t.telefon = :telefon")
    Optional<UserNew> userExists(String acstatus, String telefon);
}

