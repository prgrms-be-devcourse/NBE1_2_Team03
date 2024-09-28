package com.sscanner.team.trashcan.repository;

import com.sscanner.team.trashcan.entity.Trashcan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface TrashcanRepository extends JpaRepository<Trashcan, Long> {



    @Query(value = "SELECT *, " +
            "(6371000 * acos(cos(radians(:latitude)) * cos(radians(t.latitude)) * cos(radians(t.longitude) - radians(:longitude)) " +
            "+ sin(radians(:latitude)) * sin(radians(t.latitude)))) AS distance " +
            "FROM trashcan t " +
            "HAVING distance <= :radius " +
            "ORDER BY distance", nativeQuery = true)
    Optional<List<Trashcan>> findTrashcansWithinRadius(@Param("latitude") BigDecimal latitude,
                                                      @Param("longitude") BigDecimal longitude,
                                                      @Param("radius") double radius);


}
