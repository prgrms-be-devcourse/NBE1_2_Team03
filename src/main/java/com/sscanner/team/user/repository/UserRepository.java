package com.sscanner.team.user.repository;

import com.sscanner.team.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    boolean existsByPhone(String phone);
}
