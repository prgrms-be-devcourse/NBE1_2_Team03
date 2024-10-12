package com.sscanner.team.trashcan.entity;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.math.BigDecimal;

@Document(indexName = "trashcan")
public class TrashcanDocument {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String roadNameAddress;

    @Field(type = FieldType.Double)
    private Double latitude;

    @Field(type = FieldType.Double)
    private Double longitude;

    @Builder
    public TrashcanDocument(String id, String roadNameAddress, BigDecimal latitude, BigDecimal longitude) {
        this.id = id;
        this.roadNameAddress = roadNameAddress;
        this.latitude = latitude.doubleValue();
        this.longitude = longitude.doubleValue();
    }


}
