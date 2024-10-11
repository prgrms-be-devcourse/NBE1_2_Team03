package com.sscanner.team.payment.service;

import com.sscanner.team.barcode.entity.Barcode;
import com.sscanner.team.barcode.service.BarcodeService;
import com.sscanner.team.payment.entity.PaymentRecord;
import com.sscanner.team.payment.responsedto.PointPaymentResponseDto;
import com.sscanner.team.points.service.PointService;
import com.sscanner.team.products.entity.Product;
import com.sscanner.team.User;
import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.points.redis.PointRedisManager;
import com.sscanner.team.payment.repository.PaymentRepository;
import com.sscanner.team.payment.requestdto.PaymentRequestDto;
import com.sscanner.team.payment.requestdto.PointPaymentRequestDto;
import com.sscanner.team.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PointRedisManager pointRedisManager;
    private final ProductService productService;
    private final PointService pointService;
    private final PaymentRepository paymentRepository;
    private final BarcodeService barcodeService;

    /**
     * 포인트로 제품을 구매할 수 있습니다.*
     * @param pointPaymentRequestDto 사용자 ID, 결제할 상품의 ID
     * @return PointResponseDto 사용자 ID, 결제 이후의 사용자 Point
     */
    @Transactional
    @Override
    public PointPaymentResponseDto payPoint(PointPaymentRequestDto pointPaymentRequestDto) {
        String userId = pointPaymentRequestDto.userId();
        Long productId = pointPaymentRequestDto.productId();

        Product product = productService.getProductEntityById(productId);
        UserPoint userPoint = pointService.findUserPointByUserId(userId);
        Integer productPrice = product.getPrice();

        // Redis에서 총 포인트 가져오기
        Integer currentPoint = pointRedisManager.getPointFromRedis(userId);

        // Redis에서 포인트 차감
        validateEnoughPoints(currentPoint, productPrice);
        pointRedisManager.decrementPointInRedis(userId, productPrice);
        pointRedisManager.flagUserForBackup(userId);

        Barcode barcode = barcodeService.createAndSaveBarcode(userId, productId);

        PaymentRequestDto paymentRequestDto = PaymentRequestDto.of(
                userPoint.getUser(), product, productPrice, barcode.getBarcodeUrl()
        );
        savePayment(paymentRequestDto, userPoint.getUser(), product);

        Integer updatedPoint = pointRedisManager.getPointFromRedis(userId);
        return PointPaymentResponseDto.of(userId, updatedPoint);
    }

    private void savePayment(PaymentRequestDto paymentRequestDto, User user, Product product) {
        PaymentRecord paymentRecord = paymentRequestDto.toEntity(user, product);
        paymentRepository.save(paymentRecord);
    }

    private void validateEnoughPoints(Integer currentPoint, Integer productPrice) {
        if (currentPoint == null || currentPoint < productPrice) {
            throw new BadRequestException(ExceptionCode.NOT_ENOUGH_POINTS);
        }
    }
}
