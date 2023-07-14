package org.example.ddemo;

import org.example.utils.CpuUtils;

import java.util.concurrent.atomic.AtomicBoolean;

public class DemoC {
    public static void main(String[] args) {
        Thread aThread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("标志位被修改，终止");
                    break;
                }
                System.out.println("循环中----");
            }
        }, "A-Thread");
        aThread.start();
    
        CpuUtils.doTask(10L);
    
        new Thread(aThread::interrupt, "B-Thread").start();
    }
}
