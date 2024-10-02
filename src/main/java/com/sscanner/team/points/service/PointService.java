package com.sscanner.team.points.service;

import com.sscanner.team.points.requestdto.PointRequestDto;
import com.sscanner.team.points.responsedto.PointResponseDto;

public interface PointService {
    PointResponseDto getPoint(String userId);
    PointResponseDto addPoint(PointRequestDto pointRequestDto);
}
