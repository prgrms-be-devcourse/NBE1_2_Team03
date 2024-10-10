package com.sscanner.team.points.service;

import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.points.requestdto.PointRequestDto;
import com.sscanner.team.points.responsedto.PointResponseDto;

import java.util.Set;

public interface PointService {
    PointResponseDto getPoint(String userId);
    PointResponseDto addPoint(PointRequestDto pointRequestDto);
    void updateUserPoints(UserPoint userPoint, Integer newPoint);
    UserPoint findUserPointByUserId(String userId);
    Set<String> getFlaggedUsersForBackup();
}
