package com.sscanner.team.user.repository;

import com.sscanner.team.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByPhone(String phone);
    Optional<User> findByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM user WHERE deleted_at < :date", nativeQuery = true)
    void deleteAllByDeletedAtBefore(LocalDateTime date);// 회원 탈퇴 30일 지난 사용자
}

