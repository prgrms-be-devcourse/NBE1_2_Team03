package com.sscanner.team.trashcan;

import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.entity.TrashcanDocument;
import com.sscanner.team.trashcan.repository.TrashcanDocumentRepository;
import com.sscanner.team.trashcan.repository.TrashcanRepository;
import com.sscanner.team.trashcan.service.TrashcanDocumentService;
import com.sscanner.team.trashcan.service.TrashcanService;
import com.sscanner.team.trashcan.service.TrashcanServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TrashcanScheduler {

    private final TrashcanDocumentService trashcanDocumentService;
    private final TrashcanServiceImpl trashcanService;


    @Scheduled(fixedRate = 366000)
    public void updateTrashcanDocuments() {
        List<Trashcan> trashcans = trashcanService.findAllTrashcan();

        trashcanDocumentService.saveTrashcanDocuments(trashcans);
    }


}
