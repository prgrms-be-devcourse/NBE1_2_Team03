package com.sscanner.team.global.common.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    String makeImgUrl(MultipartFile file);
    void isExistFile(MultipartFile file);
    String separateExt(String originalFileName);
    void uploadS3(MultipartFile file, String fileName);
    String uploadBarcodeToS3(String barcodeBase64);
}
