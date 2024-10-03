package com.sscanner.team.user.scheduler;

import com.sscanner.team.user.repository.RefreshRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class RefreshTokenScheduler {

    private final RefreshRepository refreshRepository;

    // 자정마다 만료된 리프레시 토큰 삭제
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void deleteExpiredTokens() {
        Date now = new Date();
        refreshRepository.deleteExpiredTokens(now);
        System.out.println("만료된 리프레시 토큰 삭제 완료");
    }

}
