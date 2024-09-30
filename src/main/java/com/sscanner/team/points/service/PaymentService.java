package com.sscanner.team.points.service;

import com.sscanner.team.points.responsedto.PointResponseDto;

public interface PaymentService {
    PointResponseDto payPoint(String userId, Long productId);
}
