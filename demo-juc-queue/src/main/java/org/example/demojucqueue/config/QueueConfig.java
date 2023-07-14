package org.example.demojucqueue.config;

import org.example.demojucqueue.task.abstart.AbstartDelayedObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.DelayQueue;

@Configuration
public class QueueConfig {
    
    @Bean("simpleDelayQueue")
    public DelayQueue<AbstartDelayedObject> simpleDelayQueue() {
        return new DelayQueue<>();
    }
    
}
