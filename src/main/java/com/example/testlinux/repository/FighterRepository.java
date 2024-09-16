package com.example.testlinux.repository;

import com.example.testlinux.domain.Fighter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;


public interface FighterRepository extends JpaRepository<Fighter, Integer> {
    @Query("SELECT t FROM Fighter t")
    List<Fighter> getAllFightersTest();
}
