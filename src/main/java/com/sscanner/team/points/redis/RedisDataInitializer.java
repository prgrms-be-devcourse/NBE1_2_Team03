package com.sscanner.team.points.redis;

import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.points.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisDataInitializer implements CommandLineRunner {

    private final PointRepository pointRepository;
    private final PointRedisManager pointRedisManager;

    @Override
    public void run(String... args) {
        // Redis가 비어 있으면 MySQL에서 데이터를 로드
        Set<String> keys = pointRedisManager.getAllKeys();
        if (keys == null || keys.isEmpty()) {
            List<UserPoint> userPoints = pointRepository.findAllWithUser();
            for (UserPoint userPoint : userPoints) {
                String userId = userPoint.getUser().getUserId();
                Integer point = userPoint.getPoint();
                pointRedisManager.updatePointInRedis(userId, point);
            }
        }
    }
}
