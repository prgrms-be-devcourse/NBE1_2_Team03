package com.sscanner.team.user.scheduler;

import com.sscanner.team.User;
import com.sscanner.team.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDeletionScheduler {

    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    public void deleteOldUsers() {

        LocalDateTime date = LocalDateTime.now().minusDays(30);
        List<User> usersToDelete = userRepository.findAllByDeletedAt(date);

        // 기간 지난 사용자 물리적 삭제
        userRepository.deleteAll(usersToDelete);

    }
}
