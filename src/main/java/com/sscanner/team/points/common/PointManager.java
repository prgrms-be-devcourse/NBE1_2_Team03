package com.sscanner.team.points.common;

import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.points.repository.PointRepository;
import com.sscanner.team.points.requestdto.PointUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class PointManager {

    private final PointRepository pointRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    private static final String POINT_PREFIX = "POINT_";
    private static final String DAILY_POINT_PREFIX = "DAILY_POINT_";

    public UserPoint findUserPointByUserId(String userId) {
        return pointRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_USER_ID));
    }

    @Transactional
    public void updateUserPointsInDb(PointUpdateRequestDto pointUpdateRequestDto) {
        UserPoint updatedUserPoint = pointUpdateRequestDto.toEntity();
        pointRepository.save(updatedUserPoint);
    }

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

    private String getRedisKey(String userId) {
        return POINT_PREFIX + userId;
    }

    private String getDailyRedisKey(String userId) {
        return DAILY_POINT_PREFIX + userId;
    }
}
