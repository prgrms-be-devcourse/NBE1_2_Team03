package com.sscanner.team.points.service;

import com.sscanner.team.points.responsedto.PointResponseDto;

public interface PointService {
    Integer getPoint(String userId);
    PointResponseDto addPoint(String userId, Integer point);
}
