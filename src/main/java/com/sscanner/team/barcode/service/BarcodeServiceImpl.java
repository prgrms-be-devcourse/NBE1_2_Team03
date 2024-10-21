package com.sscanner.team.barcode.service;

import com.sscanner.team.barcode.common.BarcodeConstants;
import com.sscanner.team.barcode.entity.Barcode;
import com.sscanner.team.barcode.repository.BarcodeRepository;
import com.sscanner.team.barcode.responsedto.BarcodeResponseDto;
import com.sscanner.team.global.common.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class BarcodeServiceImpl implements BarcodeService {

    private final BarcodeRepository barcodeRepository;
    private final ImageService imageService;
    private final BarcodeGenerator barcodeGenerator;

    @Transactional
    public Barcode createAndSaveBarcode(String userId, Long productId) {
        String barcodeText = generateBarcodeText(userId, productId);

        String barcodeImage = generateBarcodeImage(barcodeText);

        String barcodeUrl = uploadBarcodeImage(barcodeImage);

        return saveBarcode(userId, productId, barcodeUrl);
    }

    public List<BarcodeResponseDto> findBarcodesByUserId(String userId) {
        return barcodeRepository.findAllByUserId(userId).stream()
                .map(BarcodeResponseDto::from)
                .toList();
    }

    private String uploadBarcodeImage(String barcodeImage) {
        return imageService.uploadBarcodeToS3(barcodeImage);
    }

    @NotNull
    private static String generateBarcodeText(String userId, Long productId) {
        return String.format(BarcodeConstants.BARCODE_TEXT_TEMPLATE, productId, userId);
    }

    private String generateBarcodeImage(String barcodeText) {
        return barcodeGenerator.generateBarcodeImage(barcodeText);
    }

    private Barcode saveBarcode(String userId, Long productId, String barcodeUrl) {
        Barcode barcode = Barcode.builder()
                .userId(userId)
                .productId(productId)
                .barcodeUrl(barcodeUrl)
                .build();

        return barcodeRepository.save(barcode);
    }
}
