package com.sscanner.team.global.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GeoUtils {

    private static final BigDecimal EARTH_RADIUS = new BigDecimal("6371"); // 지구 반지름
    private static final int SCALE = 8; // 최대 소수점 자리수

    // 반경을 기반으로 최소/최대 위도 및 경도 범위 계산
    // 사각형 범위의 데이터를 검색 가로, 세로 길이로 사각형을 만드는 것과 같음
    public static BigDecimal[] getBoundingBox(BigDecimal lat, BigDecimal lon, double radius) {

        // 위도 범위 계산
        BigDecimal latChange = BigDecimal.valueOf(radius / 1000).divide(EARTH_RADIUS, SCALE, RoundingMode.HALF_UP);
        BigDecimal minLat = lat.subtract(BigDecimal.valueOf(Math.toDegrees(latChange.doubleValue())));
        BigDecimal maxLat = lat.add(BigDecimal.valueOf(Math.toDegrees(latChange.doubleValue())));

        // 경도 범위 계산
        BigDecimal cosLat = BigDecimal.valueOf(Math.cos(Math.toRadians(lat.doubleValue())));
        BigDecimal lonChange = BigDecimal.valueOf(radius / 1000).divide(EARTH_RADIUS.multiply(cosLat), SCALE, RoundingMode.HALF_UP);
        BigDecimal minLon = lon.subtract(BigDecimal.valueOf(Math.toDegrees(lonChange.doubleValue())));
        BigDecimal maxLon = lon.add(BigDecimal.valueOf(Math.toDegrees(lonChange.doubleValue())));

        return new BigDecimal[]{minLat, maxLat, minLon, maxLon};
    }
}
