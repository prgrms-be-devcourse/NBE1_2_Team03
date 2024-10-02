package com.sscanner.team.points.scheduler;

import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.points.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

import static com.sscanner.team.points.common.PointConstants.DAILY_POINT_PREFIX;
import static com.sscanner.team.points.common.PointConstants.POINT_PREFIX;

@Component
@RequiredArgsConstructor
public class PointScheduler {

    private final PointRepository pointRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    @Scheduled(fixedRate = 300000, initialDelay = 300000) // 5분마다 실행
    public void backupPointsToMySQL() {
        Set<String> keys = redisTemplate.keys(POINT_PREFIX + "*");
        if (keys != null) {
            for (String key : keys) {
                String userId = key.replace(POINT_PREFIX, "");
                Integer currentPoint = redisTemplate.opsForValue().get(key);

                if (currentPoint != null) {
                    CompletableFuture.runAsync(() -> {
                        UserPoint userPoint = pointRepository.findByUserId(userId)
                                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));
                        UserPoint updatedUserPoint = userPoint.updatePoint(currentPoint);
                        pointRepository.save(updatedUserPoint);
                    });
                }
            }
        }
    }

    /**
     * 매일 00:00 일일 획득 가능 포인트를 초기화합니다.<br>
     * Redis 에 등록된 key 삭제<br>
     * key: DAILY_POINT_PREFIX + userId<br>
     * value: 금일 획득한 포인트
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyPointLimit() {
        Set<String> keys = redisTemplate.keys(DAILY_POINT_PREFIX + "*");
        if (keys != null) {
            redisTemplate.delete(keys);
        }
    }
}
