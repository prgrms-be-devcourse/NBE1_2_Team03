package com.sscanner.team.points.service;

import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.points.redis.PointRedisService;
import com.sscanner.team.points.repository.PointRepository;
import com.sscanner.team.points.requestdto.PointRequestDto;
import com.sscanner.team.points.requestdto.PointUpdateRequestDto;
import com.sscanner.team.points.responsedto.PointResponseDto;
import com.sscanner.team.points.responsedto.PointWithUserIdResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.sscanner.team.points.common.PointConstants.*;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final PointRedisService pointRedisService;
    private final PointRepository pointRepository;

    @Override
    public PointWithUserIdResponseDto getPoint(String userId) {
        Integer point = fetchAndCacheUserPoint(userId);
        return PointWithUserIdResponseDto.of(userId, point);
    }

    @Transactional
    @Override
    public PointWithUserIdResponseDto addPoint(PointRequestDto pointRequestDto) {
        String userId = pointRequestDto.userId();
        Integer point = pointRequestDto.point();

        fetchAndCacheUserPoint(userId);

        validateDailyLimitPoints(userId, point);

        updateRedisPoints(userId, point);

        markUserForBackup(userId);

        Integer updatedPoint = fetchCachedPoint(userId);
        return PointWithUserIdResponseDto.of(userId, updatedPoint);
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

    @Override
    public Integer fetchCachedPoint(String userId) {
        return pointRedisService.getPoint(userId);
    }

    private void cacheUserPoint(String userId, Integer point) {
        pointRedisService.updatePoint(userId, point);
    }

    private Integer loadUserPoint(String userId) {
        PointResponseDto pointResponseDto = findByUserId(userId);
        return pointResponseDto.toEntity().getPoint();
    }

    @Override
    public PointResponseDto findByUserId(String userId) {
        UserPoint userPoint = pointRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_USER_ID));
        return PointResponseDto.from(userPoint);
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

    @Override
    public void removeBackupFlag(String userId) {
        pointRedisService.removeBackupFlag(userId);
    }

    @Override
    public void markUserForBackup(String userId) {
        pointRedisService.flagUserForBackup(userId);
    }

    @Override
    public void decrementPoint(String userId, int point) {
        pointRedisService.decrementPoint(userId, point);
    }

    @Transactional
    @Override
    public void updateUserPoint(PointUpdateRequestDto pointUpdateRequestDto) {
        pointRepository.save(pointUpdateRequestDto.toEntity());
    }

    @Override
    public Set<String> getFlaggedUsersForBackup() {
        return pointRedisService.getFlaggedUsers();
    }

    @Override
    public void resetDailyPointsInCache() {
        pointRedisService.resetDailyPoints();
    }
}
