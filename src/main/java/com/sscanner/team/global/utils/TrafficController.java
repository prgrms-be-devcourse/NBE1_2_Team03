package com.sscanner.team.global.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/traffic")
@Slf4j
public class TrafficController {
    @Autowired
    DataSource dataSource;

    @GetMapping("/jdbc")
    public String jdbc() throws SQLException {
//        log.info("jdbc");
        Connection conn = dataSource.getConnection();
//        log.info("connection info={}", conn);
        //conn.close(); //커넥션을 닫지 않는다.
        return "ok";

    }

    @GetMapping("/cpu")
    public String cpu() {
        long value =0;
        for (long i = 0; i < 100000000000L; i++) {
            value++; }
        return "ok value=" + value;
    }

    private List<String> list = new ArrayList<>();
    @GetMapping("/jvm")
    public String jvm() {
        for (int i = 0; i < 1000000; i++) {
            list.add("hello jvm!" + i);
        }
        return "ok";
    }

}
