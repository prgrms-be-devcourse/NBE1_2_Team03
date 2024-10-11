package com.sscanner.team.barcode.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.sscanner.team.barcode.entity.Barcode;
import com.sscanner.team.barcode.repository.BarcodeRepository;
import com.sscanner.team.barcode.responsedto.BarcodeResponseDto;
import com.sscanner.team.global.common.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BarcodeService {

    private final BarcodeRepository barcodeRepository;
    private final ImageService imageService;

    public List<BarcodeResponseDto> getBarcodesByUserId(String userId) {
        List<Barcode> barcodes = barcodeRepository.findAllByUserId(userId);
        return barcodes.stream()
                .map(BarcodeResponseDto::of)
                .toList();
    }

    public Barcode createAndSaveBarcode(String userId, Long productId) {
        // 바코드 생성 (예시: zxing 사용)
        String barcodeText = "ProductID: " + productId + ", UserID: " + userId;
        String barcodeImage = generateBarcodeImage(barcodeText);

        // 바코드 이미지 S3에 저장
        String barcodeUrl = imageService.uploadBarcodeToS3(barcodeImage);

        // 바코드 정보를 DB에 저장
        Barcode barcode = Barcode.builder()
                .userId(userId)
                .productId(productId)
                .barcodeUrl(barcodeUrl)
                .build();
        return barcodeRepository.save(barcode);
    }

    private String generateBarcodeImage(String barcodeText) {
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(barcodeText, BarcodeFormat.CODE_128, 300, 100);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "png", baos);
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate barcode", e);
        }
    }
}
