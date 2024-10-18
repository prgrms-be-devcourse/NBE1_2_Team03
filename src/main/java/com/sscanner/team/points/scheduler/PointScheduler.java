package com.sscanner.team.points.scheduler;


import com.sscanner.team.points.service.PointBackupServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
@Slf4j
@RequiredArgsConstructor
public class PointScheduler {

    private final PointBackupServiceImpl pointBackupService;

    @Scheduled(cron = "0 0 3 * * ?")
    public void backupPointsToMySQL() {
        pointBackupService.backupPointsToMySQL();  // 서비스에 백업 로직을 위임
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyPointLimit() {
        pointBackupService.resetDailyPointsInCache();  // 서비스에 포인트 리셋 로직을 위임
    }
}
