package com.sscanner.team.trashcan.requestDto;

import com.sscanner.team.trashcan.type.TrashCategory;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record UpdateTrashcanRequestDto(
        @NotBlank(message = "위도는 필수입니다.")
        @DecimalMin(value = "-90.0", message = "위도는 -90 이상이어야 합니다.")
        @DecimalMax(value = "90.0", message = "위도는 90 이하이어야 합니다.")
        BigDecimal latitude,

        @NotBlank(message = "경도는 필수입니다.")
        @DecimalMin(value = "-180.0", message = "경도는 -180 이상이어야 합니다.")
        @DecimalMax(value = "180.0", message = "경도는 180 이하이어야 합니다.")
        BigDecimal longitude,

        @NotBlank(message = "도로명 주소는 필수입니다.")
        String roadNameAddress,

        String detailedAddress,

        @NotBlank(message = "카테고리는 필수입니다.")
        TrashCategory trashCategory
) {

}
