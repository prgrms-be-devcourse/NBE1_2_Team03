package com.sscanner.team.points.service;

import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.points.redis.PointRedisService;
import com.sscanner.team.points.repository.PointRepository;
import com.sscanner.team.points.dto.requestdto.PointRequestDto;
import com.sscanner.team.points.dto.responsedto.UserPointResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.sscanner.team.points.common.PointConstants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRedisService pointRedisService;
    private final PointRepository pointRepository;

    @Override
    public UserPointResponseDto getCachedPoint(String userId) {
        Integer point = fetchAndCacheUserPoint(userId);
        return UserPointResponseDto.of(userId, point);
    }

    @Transactional
    @Override
    public UserPointResponseDto addPoint(PointRequestDto pointRequestDto) {
        String userId = pointRequestDto.userId();
        Integer point = pointRequestDto.point();

        fetchAndCacheUserPoint(userId);

        validateDailyLimitPoints(userId, point);

        updateRedisPoints(userId, point);

        markUserForBackup(userId);

        Integer updatedPoint = fetchCachedPoint(userId);
        return UserPointResponseDto.of(userId, updatedPoint);
    }

    @Override
    public UserPoint findByUserId(String userId) {
        return pointRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_USER_ID));
    }

    @Override
    public void updateUserPoint(UserPoint userPoint) {
        pointRepository.save(userPoint);
    }

    @Override
    public void markUserForBackup(String userId) {
        pointRedisService.flagUserForBackup(userId);
    }

    @Override
    public void decrementPoint(String userId, int point) {
        pointRedisService.decrementPoint(userId, point);
    }

    @Override
    public Integer fetchCachedPoint(String userId) {
        return pointRedisService.getPoint(userId);
    }

    @Override
    public Integer getPoint(String userId) {
        return pointRedisService.getPoint(userId);
    }

    @Override
    public void resetDailyPoints() {
        pointRedisService.resetDailyPoints();
    }

    @Override
    public Set<String> getFlaggedUsers() {
        return pointRedisService.getFlaggedUsers();
    }

    @Override
    public void removeBackupFlag(String userId) {
        pointRedisService.removeBackupFlag(userId);
    }

    private Integer fetchAndCacheUserPoint(String userId) {
        Integer point = fetchCachedPoint(userId);

        if (isPointAbsent(point)) {
            point = loadUserPoint(userId);
            cacheUserPoint(userId, point);
        }
        return point;
    }

    private boolean isPointAbsent(Integer point) {
        return point == null;
    }

    private Integer loadUserPoint(String userId) {
        UserPoint userPoint = findByUserId(userId);
        return userPoint.getPoint();
    }

    private void cacheUserPoint(String userId, Integer point) {
        pointRedisService.updatePoint(userId, point);
    }

    private void validateDailyLimitPoints(String userId, Integer point) {
        Integer dailyPoint = pointRedisService.getDailyPoint(userId);
        if (dailyPoint + point > DAILY_LIMIT) {
            throw new BadRequestException(ExceptionCode.DAILY_POINTS_EXCEEDED);
        }
    }

    private void updateRedisPoints(String userId, Integer point) {
        pointRedisService.incrementPoint(userId, point);
        pointRedisService.incrementDailyPoint(userId, point);
    }
}
