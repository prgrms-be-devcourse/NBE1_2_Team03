package com.sscanner.team.points.service;

import com.sscanner.team.UserPoint;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.points.common.PointManager;
import com.sscanner.team.points.requestdto.PointRequestDto;
import com.sscanner.team.points.responsedto.PointResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sscanner.team.points.common.PointConstants.*;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointManager pointManager;

    /**
     * 사용자 포인트를 조회합니다.
     * @param userId 사용자 ID
     * @return PointResponseDto 사용자 Point
     */
    @Override
    public PointResponseDto getPoint(String userId) {
        Integer point = pointManager.getPointFromRedis(userId);

        if (point == null) {
            UserPoint userPoint = pointManager.findUserPointByUserId(userId);
            point = userPoint.getPoint();
            pointManager.updatePointInRedis(userId, point);
        }

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

        Integer dailyPoint = pointManager.getDailyPointFromRedis(userId);
        validateDailyLimitPoints(dailyPoint, point);

        pointManager.incrementPointInRedis(userId, point);
        pointManager.incrementDailyPointInRedis(userId, point);

        Integer updatedPoint = pointManager.getPointFromRedis(userId);
        return PointResponseDto.of(userId, updatedPoint);
    }

    private static void validateDailyLimitPoints(Integer dailyPoint, Integer point) {
        if (dailyPoint + point > DAILY_LIMIT) {
            throw new BadRequestException(ExceptionCode.DAILY_POINTS_EXCEEDED);
        }
    }
}
