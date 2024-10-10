package com.sscanner.team.payment.controller;

import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.payment.requestdto.PointPaymentRequestDto;
import com.sscanner.team.payment.responsedto.PointPaymentResponseDto;
import com.sscanner.team.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 사용자의 포인트를 사용하는 API
     * @param pointPaymentRequestDto 사용자 ID, 상품 ID
     * @return 포인트 사용 성공 메시지
     */
    @PostMapping
    public ApiResponse<PointPaymentResponseDto> payUserPoints(@RequestBody PointPaymentRequestDto pointPaymentRequestDto) {
        PointPaymentResponseDto response = paymentService.payPoint(pointPaymentRequestDto);
        return ApiResponse.ok(201, response, "포인트가 성공적으로 사용되었습니다.");
    }
}
