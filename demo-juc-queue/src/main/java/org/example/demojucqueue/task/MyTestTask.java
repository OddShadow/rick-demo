package org.example.demojucqueue.task;

import lombok.extern.slf4j.Slf4j;
import org.example.demojucqueue.entity.SysMessage;
import org.example.demojucqueue.task.abstart.AbstartDelayedObject;

import java.time.ZoneOffset;

@Slf4j
public class MyTestTask extends AbstartDelayedObject {
    
    private SysMessage sysMessage;
    
    public MyTestTask(SysMessage sysMessage) {
        super(sysMessage.getConsumeTime().toInstant(ZoneOffset.of("+8")).toEpochMilli());
        this.sysMessage = sysMessage;
    }
    
    @Override
    protected void init() {
    
    }
    
    @Override
    public void doTask() {
        log.info("===任务执行" + this);
    }
    
    public SysMessage getSysMessage() {
        return sysMessage;
    }
    
    public void setSysMessage(SysMessage sysMessage) {
        this.sysMessage = sysMessage;
    }

}
