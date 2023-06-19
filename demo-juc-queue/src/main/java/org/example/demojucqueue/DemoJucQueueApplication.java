package org.example.demojucqueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("org.example")
public class DemoJucQueueApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(DemoJucQueueApplication.class, args);
    }
    
}
