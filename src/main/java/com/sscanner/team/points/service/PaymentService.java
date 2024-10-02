package com.sscanner.team.points.service;

import com.sscanner.team.points.requestdto.PointPaymentRequestDto;
import com.sscanner.team.points.responsedto.PointResponseDto;

public interface PaymentService {
    PointResponseDto payPoint(PointPaymentRequestDto pointPaymentRequestDto);
}
