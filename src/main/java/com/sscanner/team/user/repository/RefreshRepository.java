package com.sscanner.team.user.repository;

import com.sscanner.team.user.entity.Refresh;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    Boolean existsByRefreshToken(String refreshToken);

    @Transactional
    void deleteByRefreshToken(String refreshToken);

    @Transactional
    @Modifying
    @Query("DELETE FROM Refresh r WHERE r.expiration < :currentDate")
    void deleteExpiredTokens(@Param("currentDate") Date currentDate);


}
