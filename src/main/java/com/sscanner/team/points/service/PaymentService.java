package com.sscanner.team.points.service;

import com.sscanner.team.PaymentRecord;
import com.sscanner.team.Product;
import com.sscanner.team.UserPoint;
import com.sscanner.team.points.repository.PaymentRepository;
import com.sscanner.team.points.repository.PointRepository;
import com.sscanner.team.points.responsedto.PointResponseDto;
import com.sscanner.team.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static com.sscanner.team.points.common.PointConstants.POINT_PREFIX;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PointRepository pointRepository;
    private final ProductRepository productRepository;
    private final PaymentRepository paymentRepository;
    private final RedisTemplate<String, Integer> redisTemplate;

    /**
     * 포인트로 제품을 구매할 수 있습니다.
     * @param userId 사용자 ID
     * @param productId 결제할 상품의 ID
     * @return 사용자 ID, 결제 이후의 사용자 Point
     */
    @Transactional
    public PointResponseDto payPoint(String userId, Long productId) {
        String key = POINT_PREFIX + userId;

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 상품은 존재하지 않습니다."));
        UserPoint userPoint = pointRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("해당 사용자를 찾을 수 없습니다."));

        Integer productPrice = product.getPrice();

        // Redis에서 총 포인트 가져오기
        Integer currentPoint = redisTemplate.opsForValue().get(key);
        if (currentPoint == null || currentPoint < productPrice) {
            throw new IllegalArgumentException("사용 가능한 포인트가 부족합니다.");
        }

        // Redis에서 포인트 차감
        redisTemplate.opsForValue().decrement(key, productPrice);

        CompletableFuture.runAsync(() -> {
            PaymentRecord paymentRecord = new PaymentRecord();
            paymentRecord.setPaymentRecordId(UUID.randomUUID());
            paymentRecord.setUser(userPoint.getUser());
            paymentRecord.setProduct(product);
            paymentRecord.setPayment(productPrice);

            paymentRepository.save(paymentRecord);

            userPoint.setPoint(currentPoint - productPrice);
            pointRepository.save(userPoint);
        });

        Integer updatedPoint = redisTemplate.opsForValue().get(key);
        return new PointResponseDto(userId, updatedPoint);
    }
}
