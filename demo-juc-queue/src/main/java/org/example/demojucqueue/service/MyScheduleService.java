package org.example.demojucqueue.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.demojucqueue.entity.SysMessage;
import org.example.demojucqueue.mapper.SysMessageMapper;
import org.example.demojucqueue.task.MyTestTask;
import org.example.demojucqueue.task.abstart.AbstartDelayedObject;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.DelayQueue;

@Slf4j
@Service
public class MyScheduleService {
    
    @Resource
    private DelayQueue<AbstartDelayedObject> simpleDelayQueue;
    
    @Resource
    private SysMessageMapper sysMessageMapper;
    
    public void queueService(LocalDateTime now) {
        LocalDateTime expireTime = now.plusSeconds(30);
        List<SysMessage> sysMessageList =
                sysMessageMapper.selectList(new LambdaQueryWrapper<>(SysMessage.class)
                        .eq(SysMessage::getMessage, "001")
                        .le(SysMessage::getConsumeTime, expireTime));
        for (SysMessage sysMessage : sysMessageList) {
            System.out.println(sysMessage);
            System.out.println(simpleDelayQueue.size());
            sysMessage.setConsumeTime(LocalDateTime.now().plusSeconds(8));
            simpleDelayQueue.add(new MyTestTask(sysMessage));
        }
    }
    
}
