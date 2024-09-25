package com.sscanner.team;

import com.sscanner.team.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Entity
@Table(name = "Trashcan_img")
public class TrashcanImg extends BaseEntity {
    @Id
    @Column(name = "trashcan_img_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trashcan_id", nullable = false)
    private Trashcan trashcan;

    @Column(name = "trashcan_img_url", nullable = false)
    private String trashcanImgUrl;


}