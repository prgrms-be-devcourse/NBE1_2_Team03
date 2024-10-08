package com.sscanner.team.user.repository;

import com.sscanner.team.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByPhone(String phone);
    Optional<User> findByEmail(String email);
    List<User> findAllByDeletedAt(LocalDateTime dateTime); // 회원 탈퇴 30일 지난 사용자
}

