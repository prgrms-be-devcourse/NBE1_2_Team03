package com.sscanner.team.user.repository;

import com.sscanner.team.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String eamil);
    boolean existsByNickname(String nickname);
    boolean existsByPhone(String phone);
    Optional<User> findByEmail(String email);

}

