package com.example.testlinux.repository;

import com.example.testlinux.domain.UserNew;
import com.example.testlinux.dto.UserEntityDto;
import com.example.testlinux.dto.UserEntityFighterDto;
import com.example.testlinux.dto.UserFighterDto;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.example.testlinux.dto.UserDto;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserNewRepository extends CrudRepository<UserNew, Integer> {
    @Query("SELECT t FROM UserNew t")
    List<UserNew> findAllUsers();

    @EntityGraph(attributePaths = {"fighter", "judge", "trainer"})
    Optional<UserNew> findUserById(Integer login);

/*    @Query(value = "SELECT u.id AS user_id, u.pass, u.telefon, " +
            "f.id AS fighter_id, f.first_name, f.last_name " +
            "FROM users u " +
            "LEFT JOIN fighters f ON u.id = f.login " +
            "WHERE u.id = :login", nativeQuery = true)
    List<UserFighterDto> findUserById(@Param("login") Integer login);*/

    //@Query("SELECT u FROM UserNew u WHERE u.id = :id")
    //@Query("SELECT u FROM UserNew u JOIN FETCH u.fighter JOIN FETCH u.judge JOIN FETCH u.trainer WHERE u.id = :id")
    //Optional<UserNew> getUserById(Integer id);

    @Query("SELECT new com.example.testlinux.dto.UserDto(t.fighter.firstName) FROM UserNew t WHERE t.id = :id")
    Optional<UserDto> getUserFast(Long id);

    /*@Query("SELECT new com.example.testlinux.dto.UserEntityDto(u.fighter, u.judge, u.trainer) FROM UserNew u " +
     *//*"LEFT JOIN fetch Fighter f ON u.id = f.user.id " +
            "LEFT JOIN fetch Judge j ON u.id = j.user.id " +
            "LEFT JOIN fetch Trainer t ON u.id = t.user.id " +*//*
            "LEFT JOIN u.fighter f " +
            "LEFT JOIN u.judge j " +
            "LEFT JOIN u.trainer t " +
            "WHERE u.id = :id")
    Optional<UserEntityDto> getRole(Long id);*/

    @Query("SELECT new com.example.testlinux.dto.UserEntityDto(u.fighter) FROM UserNew u " +
            "LEFT JOIN u.fighter f " +
            "WHERE u.id = :id")
    Optional<UserEntityDto> getRole(Integer id);

    @Query("SELECT new com.example.testlinux.dto.UserEntityFighterDto(u.fighter) FROM UserNew u " +
            "LEFT JOIN u.fighter f " +
            "WHERE u.id = :id")
    Optional<UserEntityFighterDto> getFighter(Integer id);

    /*@Query("SELECT t FROM UserNew t WHERE t.id = :id")
    List<UserNew> getUser(String id);*/

    @Query("SELECT t FROM UserNew t WHERE t.pass = :acstatus AND t.telefon = :telefon")
    Optional<UserNew> userExists(String acstatus, String telefon);
}

