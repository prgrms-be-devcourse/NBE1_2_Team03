package com.sscanner.team.trashcan.service;

import com.sscanner.team.trashcan.entity.TrashcanImg;
import org.springframework.web.multipart.MultipartFile;

public interface TrashcanImgService {

    String uploadTrashcanImg(MultipartFile file);

    TrashcanImg saveTrashcanImg(Long trashcanId, String imgUrl);

    TrashcanImg getTrashcanImg(Long trashcanImgId);
    void deleteTrashcanImg(Long trashcanImgId);
}
