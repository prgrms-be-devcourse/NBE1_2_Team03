package com.sscanner.team.points.service;

import com.sscanner.team.UserPoint;
import com.sscanner.team.points.repository.PointRepository;
import com.sscanner.team.points.responsedto.PointResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.sscanner.team.points.common.PointConstants.*;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRepository pointRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    /**
     * 사용자 포인트를 조회합니다.
     * @param userId 사용자 ID
     * @return 사용자 Point
     */
    @Override
    public Integer getPoint(String userId) {
        String key = POINT_PREFIX + userId;
        Integer point = redisTemplate.opsForValue().get(key);

        if (point == null) {
            UserPoint userPoint = pointRepository.findByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));
            point = userPoint.getPoint();
            redisTemplate.opsForValue().set(key, point, 1, TimeUnit.DAYS);
        }

        return point;
    }

    /**
     * 사용자에게 포인트를 제공합니다.
     * @param userId 사용자 ID
     * @param point 추가할 Point
     * @return 사용자 ID, 변경된 사용자 Point
     */
    @Transactional
    @Override
    public PointResponseDto addPoint(String userId, Integer point) {
        String key = POINT_PREFIX + userId;
        String dailyKey = DAILY_POINT_PREFIX + userId;

//        Integer currentPoint = getCachedPoint(key);
//        if (currentPoint == null) {
//            currentPoint = getPoint(userId);
//        }

        Integer dailyPoint = getCachedPoint(dailyKey);

        if (dailyPoint + point > DAILY_LIMIT) {
            throw new IllegalArgumentException("일일 획득 포인트 초과");
        }

        redisTemplate.opsForValue().increment(key, point);
        redisTemplate.opsForValue().increment(dailyKey, point);

        Integer updatedPoint = redisTemplate.opsForValue().get(key);
        return new PointResponseDto(userId, updatedPoint);
    }

    /**
     * Redis에서 캐싱된 포인트 가져오기. 값이 null일 경우 0 반환.
     * @param key Redis Key
     * @return Point
     */
    private Integer getCachedPoint(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key)).orElse(0);
    }
}
