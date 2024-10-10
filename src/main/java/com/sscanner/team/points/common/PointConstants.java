package com.sscanner.team.points.common;

public class PointConstants {

    public static final String POINT_PREFIX = "point:";
    public static final String DAILY_POINT_PREFIX = "daily_point:";
    public static final String BACKUP_FLAG_PREFIX = "backup_flag:";
    public static final Integer DAILY_LIMIT = 100000;
    public static final Integer RETRY_MAX_ATTEMPTS = 3;

    private PointConstants() {
        throw new AssertionError("상수 클래스이기 때문에 인스턴스화할 수 없습니다.");
    }
}
