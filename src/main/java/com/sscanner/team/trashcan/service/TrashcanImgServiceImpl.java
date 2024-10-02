package com.sscanner.team.trashcan.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sscanner.team.trashcan.entity.TrashcanImg;
import com.sscanner.team.trashcan.repository.TrashcanImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RequiredArgsConstructor
@Service
public class TrashcanImgServiceImpl implements TrashcanImgService {

    @Value("${sscanner.bucket.base-url}")
    private String baseUrl;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3 s3Client;

    private final TrashcanImgRepository trashcanImgRepository;


    @Override
    public String uploadTrashcanImg(MultipartFile file) {

        return uploadFile(file);
    }

    @Override
    public TrashcanImg saveTrashcanImg(Long trashcanId, String imgUrl) {

        TrashcanImg trashcanImg = new TrashcanImg(trashcanId, imgUrl);

        return trashcanImgRepository.save(trashcanImg);
    }

    @Override
    public TrashcanImg getTrashcanImg(Long trashcanId) {

        return getTrashcanImgByTrashcanId(trashcanId);
    }

    private TrashcanImg getTrashcanImgByTrashcanId(Long trashcanId){
        return trashcanImgRepository.findByTrashcanId(trashcanId)
                .orElseThrow(null);
    }

    @Override
    public void deleteTrashcanImg(Long trashcanImgId) {

    }

    // 여러개의 파일 업로드
    public String uploadFile(MultipartFile file) {

        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try(InputStream inputStream = file.getInputStream()) {
            s3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch(IOException e) {
            throw new IllegalArgumentException("이미지 등록 실패");
        }

        return baseUrl + "/" + fileName;
    }

    // 파일명 중복 방지 (UUID)
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    // 파일 유효성 검사
    private String getFileExtension(String fileName) {
        if (fileName.length() == 0) {
            throw new IllegalArgumentException("File name cannot be empty");
        }
        ArrayList<String> fileValidate = new ArrayList<>();
        fileValidate.add(".jpg");
        fileValidate.add(".jpeg");
        fileValidate.add(".png");
        fileValidate.add(".JPG");
        fileValidate.add(".JPEG");
        fileValidate.add(".PNG");
        String idxFileName = fileName.substring(fileName.lastIndexOf("."));
        if (!fileValidate.contains(idxFileName)) {
            throw new IllegalArgumentException("File extension is not valid");
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
