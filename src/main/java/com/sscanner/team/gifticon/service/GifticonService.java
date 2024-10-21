package com.sscanner.team.gifticon.service;

import com.sscanner.team.barcode.entity.Barcode;
import com.sscanner.team.barcode.repository.BarcodeRepository;
import com.sscanner.team.gifticon.responsedto.GifticonResponseDto;
import com.sscanner.team.products.entity.Product;
import com.sscanner.team.products.service.ProductImgService;
import com.sscanner.team.products.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class GifticonService {

    private final BarcodeRepository barcodeRepository;
    private final ProductService productService;
    private final ProductImgService productImgService;

    /**
     * 사용자별 기프티콘 정보를 제공하는 메서드 (대표 상품 이미지를 포함)
     * @param userId 사용자 ID
     * @return List<GifticonResponseDto> 기프티콘 목록
     */
    public List<GifticonResponseDto> getGifticonsByUserId(String userId) {
        // 사용자 바코드 조회
        List<Barcode> barcodes = barcodeRepository.findAllByUserId(userId);

        // 각 바코드에 해당하는 상품 및 대표 이미지 정보 조회
        return barcodes.stream()
                .map(barcode -> {
                    Product product = productService.findById(barcode.getProductId());
                    String representativeProductImgUrl = productImgService.findMainImageUrl(barcode.getProductId());
                    return GifticonResponseDto.of(product, representativeProductImgUrl, barcode.getBarcodeUrl());
                })
                .toList();
    }
}
