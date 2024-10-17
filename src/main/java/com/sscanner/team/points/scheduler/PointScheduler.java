package com.sscanner.team.points.scheduler;

import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.points.requestdto.PointUpdateRequestDto;
import com.sscanner.team.points.responsedto.PointResponseDto;
import com.sscanner.team.points.service.PointService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.sscanner.team.points.common.PointConstants.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class PointScheduler {

    private final PointService pointService;
    private final ThreadPoolTaskExecutor taskExecutor;
    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5); // 비동기 스케줄링 서비스
    private final ReentrantLock lock = new ReentrantLock();  // 동시성 제어를 위한 Lock

    @PostConstruct
    public void init() {
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(50);
        taskExecutor.setQueueCapacity(100);
        taskExecutor.setThreadNamePrefix("Async-Task-");
        taskExecutor.initialize();
    }

    @Scheduled(cron = "0 0 3 * * ?")
    public void backupPointsToMySQL() {
        Set<String> flaggedUsers = pointService.getFlaggedUsersForBackup();
        runBackupAsyncForUsers(flaggedUsers);
    }

    private void runBackupAsyncForUsers(Set<String> flaggedUsers) {
        for (String flaggedUserId : flaggedUsers) {
            String userId = flaggedUserId.replace(BACKUP_FLAG_PREFIX, "");
            CompletableFuture.runAsync(() -> processPointBackup(userId), taskExecutor)
                    .exceptionally(ex -> {
                        log.error("Backup failed for user {}", userId, ex);
                        return null;
                    });
        }
    }

    private void processPointBackup(String userId) {
        retryAsync(() -> backupUserPoints(userId), RETRY_MAX_ATTEMPTS, RETRY_DELAY);
    }

    private void retryAsync(Runnable task, int attempts, long delay) {
        CompletableFuture.runAsync(() -> {
            try {
                task.run();  // 시도
            } catch (Exception e) {
                handleRetryFailure(task, attempts, delay, e);
            }
        }, taskExecutor);
    }

    private void handleRetryFailure(Runnable task, int attempts, long delay, Exception e) {
        if (attempts > 0) {
            log.error("Attempt failed, retrying... Attempts left: {}, delay: {}ms", attempts, delay, e);
            scheduledExecutorService.schedule(() -> retryAsync(task, attempts - 1, delay * 2), delay, TimeUnit.MILLISECONDS);
        } else {
            log.error("Max retry attempts reached. Task failed.");
        }
    }

    private void backupUserPoints(String userId) {
        // Lock 범위를 최소화하여, 갱신 작업과 플래그 제거에만 적용
        Integer currentCachedPoint = fetchCachedPoint(userId);
        if (currentCachedPoint != null) {
            updateUserPointsWithLock(userId, currentCachedPoint);
        }
        removeBackupFlagWithLock(userId);
    }

    private Integer fetchCachedPoint(String userId) {
        return pointService.fetchCachedPoint(userId);
    }

    private void updateUserPointsWithLock(String userId, Integer currentCachedPoint) {
        lock.lock();
        try {
            PointUpdateRequestDto updateRequestDto = createPointUpdateRequestDto(userId, currentCachedPoint);
            updateUserPoint(updateRequestDto);
        } finally {
            lock.unlock();
        }
    }

    private PointUpdateRequestDto createPointUpdateRequestDto(String userId, Integer currentCachedPoint) {
        PointResponseDto pointResponseDto = pointService.findByUserId(userId);
        UserPoint userPoint = pointResponseDto.toEntity();

        return PointUpdateRequestDto.of(userPoint.getId(), userPoint.getUser(), currentCachedPoint);
    }

    private void updateUserPoint(PointUpdateRequestDto updateRequestDto) {
        pointService.updateUserPoint(updateRequestDto);
    }

    private void removeBackupFlagWithLock(String userId) {
        lock.lock();
        try {
            removeBackupFlag(userId);
        } finally {
            lock.unlock();
        }
    }

    private void removeBackupFlag(String userId) {
        pointService.removeBackupFlag(userId);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyPointLimit() {
        pointService.resetDailyPointsInCache();
    }

    @PreDestroy
    public void shutdown() {
        taskExecutor.shutdown();
        try {
            if (!hasTaskExecutorTerminated(taskExecutor)) {
                taskExecutor.getThreadPoolExecutor().shutdownNow();
            }
        } catch (InterruptedException ex) {
            taskExecutor.getThreadPoolExecutor().shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private boolean hasTaskExecutorTerminated(ThreadPoolTaskExecutor taskExecutor) throws InterruptedException {
        return taskExecutor.getThreadPoolExecutor().awaitTermination(TIME_OUT, TimeUnit.SECONDS);
    }
}
