package com.sscanner.team.points.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.sscanner.team.points.common.PointConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointRedisManager {

    private final RedisTemplate<String, Integer> redisTemplate;

    public Integer getPointFromRedis(String userId) {
        return redisTemplate.opsForValue().get(getRedisKey(userId));
    }

    public void updatePointInRedis(String userId, Integer point) {
        redisTemplate.opsForValue().set(getRedisKey(userId), point, 1, TimeUnit.DAYS);
    }

    public void incrementPointInRedis(String userId, Integer incrementValue) {
        redisTemplate.opsForValue().increment(getRedisKey(userId), incrementValue);
    }

    public void decrementPointInRedis(String userId, Integer decrementValue) {
        redisTemplate.opsForValue().decrement(getRedisKey(userId), decrementValue);
    }

    public Integer getDailyPointFromRedis(String userId) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(getDailyRedisKey(userId))).orElse(0);
    }

    public void incrementDailyPointInRedis(String userId, Integer incrementValue) {
        redisTemplate.opsForValue().increment(getDailyRedisKey(userId), incrementValue);
    }

    // 플래그를 저장하기 위한 Integer 사용 (1 = true, 0 = false)
    public void flagUserForBackup(String userId) {
        redisTemplate.opsForValue().set(BACKUP_FLAG_PREFIX + userId, 1);
        log.error("{}", redisTemplate.opsForValue().get(BACKUP_FLAG_PREFIX + userId));
    }

    private String getRedisKey(String userId) {
        return POINT_PREFIX + userId;
    }

    public Set<String> getFlaggedUsers() {
        return redisTemplate.keys( BACKUP_FLAG_PREFIX+"*");
    }

    public Set<String> getAllKeys() {
        return redisTemplate.keys(POINT_PREFIX + "*");
    }

    private String getDailyRedisKey(String userId) {
        return DAILY_POINT_PREFIX + userId;
    }
}
