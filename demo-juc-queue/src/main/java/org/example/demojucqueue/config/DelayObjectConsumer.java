package org.example.demojucqueue.config;

import lombok.extern.slf4j.Slf4j;
import org.example.demojucqueue.task.abstart.AbstartDelayedObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.concurrent.DelayQueue;

@Slf4j
@Component
public class DelayObjectConsumer implements InitializingBean {
    
    @Resource
    private DelayQueue<AbstartDelayedObject> simpleDelayQueue;
    
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("===== 启动线程 =====");
        new Thread(() -> {
            while (true) {
                log.info(LocalDateTime.now() + "::线程::" + Thread.currentThread().getName() + "::开始执行");
                AbstartDelayedObject task = null;
                try {
                    task = simpleDelayQueue.take();
                    task.doTask();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "simple-delay-queue").start();
    }
    
}
