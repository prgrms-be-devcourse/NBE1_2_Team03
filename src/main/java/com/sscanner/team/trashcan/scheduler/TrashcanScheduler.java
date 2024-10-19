package com.sscanner.team.trashcan.scheduler;

import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.service.TrashcanDocumentService;
import com.sscanner.team.trashcan.service.TrashcanServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TrashcanScheduler {

    private final TrashcanDocumentService trashcanDocumentService;
    private final TrashcanServiceImpl trashcanService;


    @Scheduled(fixedRate = 666000)
    public void updateTrashcanDocuments() {
        List<Trashcan> trashcans = trashcanService.findAllTrashcan();

        trashcanDocumentService.saveTrashcanDocuments(trashcans);
    }


}
