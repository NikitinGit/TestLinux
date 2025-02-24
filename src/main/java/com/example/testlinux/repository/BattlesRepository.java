package com.example.testlinux.repository;

import com.example.testlinux.domain.Battle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BattlesRepository extends JpaRepository<Battle, Long> {

    //@Lock(LockModeType.OPTIMISTIC)
    @Query("SELECT t FROM Battle t WHERE t.idBattle = :battleId")
    Optional<Battle> getOpenEventBattleByBattleId(Long battleId);

    @Modifying
    @Query("UPDATE Battle b SET b.sectionNumber = b.sectionNumber + 1 WHERE b.idBattle = :battleId")
    void updateBattle(Long battleId);
}
