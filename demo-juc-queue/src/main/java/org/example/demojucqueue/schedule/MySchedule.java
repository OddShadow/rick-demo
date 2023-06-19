package org.example.demojucqueue.schedule;

import lombok.extern.slf4j.Slf4j;
import org.example.demojucqueue.service.MyScheduleService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Slf4j
@Component
public class MySchedule {
    
    @Resource
    private MyScheduleService myScheduleService;
    
    @Scheduled(cron = "0/10 * * * * ?")
    public void QueueController() {
        LocalDateTime now = LocalDateTime.now();
        log.info("定时执行，时间{}", now);
        myScheduleService.queueService(now);
    }

}
