package com.sscanner.team.points.service;

import com.sscanner.team.points.entity.PaymentRecord;
import com.sscanner.team.products.entity.Product;
import com.sscanner.team.User;
import com.sscanner.team.points.entity.UserPoint;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import com.sscanner.team.points.common.PointManager;
import com.sscanner.team.points.repository.PaymentRepository;
import com.sscanner.team.points.requestdto.PaymentRequestDto;
import com.sscanner.team.points.requestdto.PointPaymentRequestDto;
import com.sscanner.team.points.requestdto.PointUpdateRequestDto;
import com.sscanner.team.points.responsedto.PointResponseDto;
import com.sscanner.team.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PointManager pointManager;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;

    /**
     * 포인트로 제품을 구매할 수 있습니다.*
     * @param pointPaymentRequestDto 사용자 ID, 결제할 상품의 ID
     * @return PointResponseDto 사용자 ID, 결제 이후의 사용자 Point
     */
    @Transactional
    @Override
    public PointResponseDto payPoint(PointPaymentRequestDto pointPaymentRequestDto) {
        String userId = pointPaymentRequestDto.userId();
        Long productId = pointPaymentRequestDto.productId();

        Product product = findByProductId(productId);
        UserPoint userPoint = pointManager.findUserPointByUserId(userId);
        Integer productPrice = product.getPrice();

        // Redis에서 총 포인트 가져오기
        Integer currentPoint = pointManager.getPointFromRedis(userId);
        validateEnoughPoints(currentPoint, productPrice);

        // Redis에서 포인트 차감
        pointManager.decrementPointInRedis(userId, productPrice);

        processPaymentAsync(userPoint, product, productPrice, currentPoint);

        Integer updatedPoint = pointManager.getPointFromRedis(userId);
        return PointResponseDto.of(userId, updatedPoint);
    }

    private void processPaymentAsync(UserPoint userPoint, Product product, Integer productPrice, Integer currentPoint) {
        CompletableFuture.runAsync(() -> {
            try {
                PaymentRequestDto paymentRequestDto = PaymentRequestDto.of(userPoint.getUser(), product, productPrice);
                savePayment(paymentRequestDto, userPoint.getUser(), product);

                PointUpdateRequestDto pointUpdateRequestDto = PointUpdateRequestDto.of(
                        userPoint.getUserPointId(),
                        userPoint.getUser(),
                        currentPoint - productPrice
                );

                pointManager.updateUserPointsInDb(pointUpdateRequestDto);
            } catch (Exception e) {
                throw new RuntimeException("비동기 작업 중 오류 발생", e);
            }
        });
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

    private Product findByProductId(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new BadRequestException(ExceptionCode.NOT_FOUND_ITEM_ID));
    }
}
