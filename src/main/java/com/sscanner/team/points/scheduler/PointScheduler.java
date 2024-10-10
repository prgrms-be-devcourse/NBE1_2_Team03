package com.sscanner.team.points.scheduler;

import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.points.service.PointService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.sscanner.team.points.common.PointConstants.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class PointScheduler {

    private final PointService pointService;
    private final RedisTemplate<String, Integer> redisTemplate;
    private final ThreadPoolTaskExecutor taskExecutor;

    @PostConstruct
    public void init() {
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(50);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setThreadNamePrefix("Async-Task-");
        taskExecutor.initialize();
    }

    @Scheduled(fixedRate = 300000, initialDelay = 300000)
    public void backupPointsToMySQL() {
        Set<String> flaggedUsers = pointService.getFlaggedUsersForBackup();
        log.error(flaggedUsers.toString());

        for (String flaggedUserId : flaggedUsers) {
            String userId = flaggedUserId.replace(BACKUP_FLAG_PREFIX, "");
            CompletableFuture.runAsync(() -> processUserBackup(userId), taskExecutor)
                    .exceptionally(ex -> null);
        }
    }

    private void processUserBackup(String userId) {
        retryOnFailure(() -> {
            Integer currentPoint = redisTemplate.opsForValue().get(POINT_PREFIX + userId);
            if (currentPoint != null) {
                UserPoint userPoint = pointService.findUserPointByUserId(userId);
                pointService.updateUserPoints(userPoint, currentPoint);
            }
            // 백업이 완료된 후, 플래그를 삭제
            redisTemplate.delete(BACKUP_FLAG_PREFIX + userId);
        });
    }

    private void retryOnFailure(Runnable task) {
        int attempts = 0;

        while (attempts < RETRY_MAX_ATTEMPTS) {
            try {
                task.run();
                return;
            } catch (Exception e) {
                attempts++;
                if (attempts >= RETRY_MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException interruptedException) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyPointLimit() {
        ScanOptions scanOptions = ScanOptions.scanOptions().match(DAILY_POINT_PREFIX + "*").build();

        try (Cursor<String> cursor = redisTemplate.scan(scanOptions)) {
            while (cursor.hasNext()) {
                String key = cursor.next();
                redisTemplate.delete(key);
            }
        } catch (Exception e) {
            log.error("Error during Redis SCAN operation", e);
        }
    }

    @PreDestroy
    public void shutdown() {
        taskExecutor.shutdown();
        try {
            if (!taskExecutor.getThreadPoolExecutor().awaitTermination(60, TimeUnit.SECONDS)) {
                taskExecutor.getThreadPoolExecutor().shutdownNow();
            }
        } catch (InterruptedException ex) {
            taskExecutor.getThreadPoolExecutor().shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
