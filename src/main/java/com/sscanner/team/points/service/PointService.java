package com.sscanner.team.points.service;

import com.sscanner.team.points.requestdto.PointRequestDto;
import com.sscanner.team.points.requestdto.PointUpdateRequestDto;
import com.sscanner.team.points.responsedto.PointResponseDto;
import com.sscanner.team.points.responsedto.PointWithUserIdResponseDto;

import java.util.Set;

public interface PointService {
    PointWithUserIdResponseDto getPoint(String userId);
    PointWithUserIdResponseDto addPoint(PointRequestDto pointRequestDto);
    Integer fetchCachedPoint(String userId);
    PointResponseDto findByUserId(String userId);
    void removeBackupFlag(String userId);
    void updateUserPoint(PointUpdateRequestDto pointUpdateRequestDto);
    Set<String> getFlaggedUsersForBackup();
    void resetDailyPointsInCache();
}
