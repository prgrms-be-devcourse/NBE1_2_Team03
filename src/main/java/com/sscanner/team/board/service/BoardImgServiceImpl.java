package com.sscanner.team.board.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.sscanner.team.board.entity.Board;
import com.sscanner.team.board.entity.BoardImg;
import com.sscanner.team.board.repository.BoardImgRepository;
import com.sscanner.team.global.exception.BadRequestException;
import com.sscanner.team.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.sscanner.team.global.exception.ExceptionCode.*;

@Service
@RequiredArgsConstructor
public class BoardImgServiceImpl implements BoardImgService{

    private final BoardImgRepository boardImgRepository;
    private final AmazonS3 amazonS3;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.s3.base-url}")
    private String baseUrl;

    @Override
    public void saveBoardImg(Board board, MultipartFile file) {
        String boardImgUrl = makeImgUrl(file);

        BoardImg boardImg = BoardImg.makeBoardImg(board, boardImgUrl);

        boardImgRepository.save(boardImg);
    }

    @Override
    public String makeImgUrl(MultipartFile file) {
        isExistFile(file);

        String originalFileName = file.getOriginalFilename();

        String ext = separateExt(originalFileName);

        String randomName = UUID.randomUUID().toString();
        String fileName = randomName + "." + ext;

        uploadS3(file, fileName); // S3에 이미지 업로드

        return baseUrl + "/" + fileName;
    }

    /**
     * 파일 존재 여부 확인
     * @param file - 이미지 파일
     */
    private void isExistFile(MultipartFile file) {
        if(file.isEmpty() && file.getOriginalFilename() != null) {
            throw new BadRequestException(NOT_EXIST_FILE);
        }
    }

    /**
     * 파일 원본 이름에서 확장자 분리하고 이미지 확장자인지 확인
     * @param originalFileName - 원본 파일 이름
     * @return 확장자
     */
    private String separateExt(String originalFileName) {
        String ext = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);

        if (!(ext.equals("jpg")
                || ext.equals("HEIC")
                || ext.equals("jpeg")
                || ext.equals("png")
                || ext.equals("heic"))) {
            throw new BadRequestException(BAD_FILE_EXTENSION);
        }
        return ext;
    }

    /**
     * S3에 이미지 파일 업로드
     * @param file - 이미지 파일
     * @param fileName - 새로 생성한 파일 이름
     */
    private void uploadS3(MultipartFile file, String fileName) {
        try {
            ObjectMetadata objMeta = new ObjectMetadata();
            byte[] bytes = IOUtils.toByteArray(file.getInputStream()); // 파일의 데이터를 바이트로 읽습니다.
            objMeta.setContentType(file.getContentType()); // 파일의 타입을 지정합니다.예) text, image
            objMeta.setContentLength(bytes.length); // 파일의 크기

            // PutObjectRequest를 사용하여 지정된 S3 버킷에 파일을 업로드
            amazonS3.putObject(
                    new PutObjectRequest(bucket, fileName, file.getInputStream(), objMeta)
                            .withCannedAcl(CannedAccessControlList.PublicRead)); // ACL을 PublicRead로 하여 공용으로 읽을 수 있도록 권한 부여
        } catch (IOException e) {
            throw new BadRequestException(FILE_UPLOAD_FAIL);
        }
    }
}
