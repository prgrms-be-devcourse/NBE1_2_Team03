package com.sscanner.team.trashcan.repository;

import com.sscanner.team.trashcan.entity.TrashcanDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface TrashcanDocumentRepository extends ElasticsearchRepository<TrashcanDocument, String> {
    List<TrashcanDocument> findByRoadNameAddress(String roadNameAddress);
}

