package com.sscanner.team;

import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Entity
public class Trashcan extends BaseEntity {
    @Id
    @Column(name = "trashcan_id", nullable = false)
    private Long id;

    @Column(name = "latitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "road_name_address", nullable = false, length = 100)
    private String roadNameAddress;

    @Column(name = "detailed_address", nullable = false, length = 100)
    private String detailedAddress;

    @Column(name = "trash_category", nullable = false, length = 15)
    private String trashCategory;

    @ColumnDefault("'EMPTY'")
    @Column(name = "trashcan_status", nullable = false, length = 10)
    private String trashcanStatus;


}