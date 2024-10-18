package com.sscanner.team.points.service;

import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.points.dto.requestdto.PointRequestDto;
import com.sscanner.team.points.dto.responsedto.UserPointResponseDto;

import java.util.Set;


public interface PointService {
    UserPointResponseDto getCachedPoint(String userId);
    UserPointResponseDto addPoint(PointRequestDto pointRequestDto);
    UserPoint findByUserId(String userId);
    void markUserForBackup(String userId);
    void updateUserPoint(UserPoint userPoint);

    void decrementPoint(String userId, int productPrice);
    Integer fetchCachedPoint(String userId);
    Integer getPoint(String userId);
    void resetDailyPoints();
    Set<String> getFlaggedUsers();
    void removeBackupFlag(String userId);
}
