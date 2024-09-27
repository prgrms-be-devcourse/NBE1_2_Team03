package com.sscanner.team.trashcan.repository;

import com.sscanner.team.trashcan.entity.Trashcan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrashcanRepository extends JpaRepository<Trashcan, Long> {
}
