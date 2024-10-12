package com.sscanner.team.trashcan.service;

import com.sscanner.team.trashcan.entity.Trashcan;
import com.sscanner.team.trashcan.entity.TrashcanDocument;
import com.sscanner.team.trashcan.repository.TrashcanDocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TrashcanDocumentService {

    private final TrashcanDocumentRepository documentRepository;

    public TrashcanDocument save(TrashcanDocument document) {
        return documentRepository.save(document);
    }

    public List<TrashcanDocument> findByRoadNameAddress(String roadNameAddress) {
        return documentRepository.findByRoadNameAddress(roadNameAddress);
    }

    public void saveTrashcanDocuments(List<Trashcan> trashcans) {
        List<TrashcanDocument> trashcanDocuments = convertTrashcansToDocuments(trashcans);
        documentRepository.saveAll(trashcanDocuments);
    }

    private List<TrashcanDocument> convertTrashcansToDocuments(List<Trashcan> trashcans) {

        List<TrashcanDocument> documents = new ArrayList<>();

        for (Trashcan trashcan : trashcans) {

            TrashcanDocument document = TrashcanDocument.builder()
                    .id(trashcan.getId().toString())
                    .roadNameAddress(trashcan.getRoadNameAddress())
                    .longitude(trashcan.getLongitude())
                    .latitude(trashcan.getLatitude())
                    .build();

            documents.add(document);
        }
        return documents;
    }



}

