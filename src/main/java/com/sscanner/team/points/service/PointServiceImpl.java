package com.sscanner.team.points.service;

import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.points.redis.PointRedisManager;
import com.sscanner.team.points.repository.PointRepository;
import com.sscanner.team.points.requestdto.PointRequestDto;
import com.sscanner.team.points.requestdto.PointUpdateRequestDto;
import com.sscanner.team.points.responsedto.PointResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.sscanner.team.points.common.PointConstants.*;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRedisManager pointRedisManager;
    private final PointRepository pointRepository;

    /**
     * 사용자 포인트를 조회합니다.
     * @param userId 사용자 ID
     * @return PointResponseDto 사용자 Point
     */
    @Override
    public PointResponseDto getPoint(String userId) {
        Integer point = getOrUpdatePoint(userId);

        return PointResponseDto.of(userId, point);
    }

    /**
     * 사용자에게 포인트를 제공합니다.
     * @param pointRequestDto 사용자 ID, 추가할 Point
     * @return pointResponseDto 사용자 ID, 변경된 사용자 Point
     */
    @Transactional
    @Override
    public PointResponseDto addPoint(PointRequestDto pointRequestDto) {
        String userId = pointRequestDto.userId();
        Integer point = pointRequestDto.point();

        // 먼저 Redis에 사용자가 있는지 확인, 없으면 MySQL에서 가져와 Redis에 저장
        getOrUpdatePoint(userId);

        Integer dailyPoint = pointRedisManager.getDailyPointFromRedis(userId);
        validateDailyLimitPoints(dailyPoint, point);

        pointRedisManager.incrementPointInRedis(userId, point);
        pointRedisManager.incrementDailyPointInRedis(userId, point);

        pointRedisManager.flagUserForBackup(userId);

        Integer updatedPoint = pointRedisManager.getPointFromRedis(userId);
        return PointResponseDto.of(userId, updatedPoint);
    }

    @Transactional
    @Override
    public void updateUserPoints(UserPoint userPoint, Integer newPoint) {
        // PointUpdateRequestDto를 생성하여 DB에 반영
        PointUpdateRequestDto pointUpdateRequestDto = PointUpdateRequestDto.of(
                userPoint.getUserPointId(),
                userPoint.getUser(),
                newPoint
        );

        pointRepository.save(pointUpdateRequestDto.toEntity());
    }

    @Override
    public UserPoint findUserPointByUserId(String userId) {
        return pointRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_USER_ID));
    }

    private Integer getOrUpdatePoint(String userId) {
        Integer point = pointRedisManager.getPointFromRedis(userId);

        if (point == null) {
            UserPoint userPoint = findUserPointByUserId(userId);
            point = userPoint.getPoint();
            pointRedisManager.updatePointInRedis(userId, point);
        }
        return point;
    }

    private static void validateDailyLimitPoints(Integer dailyPoint, Integer point) {
        if (dailyPoint + point > DAILY_LIMIT) {
            throw new BadRequestException(ExceptionCode.DAILY_POINTS_EXCEEDED);
        }
    }

    @Override
    public Set<String> getFlaggedUsersForBackup() {
        return pointRedisManager.getFlaggedUsers();
    }
}
