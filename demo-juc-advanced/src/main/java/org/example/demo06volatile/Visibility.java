package org.example.demo06volatile;

import static org.example.utils.CpuUtils.doTask;

public class Visibility {
    private static volatile boolean ready;
    private static volatile int number;
    public static void main(String[] args) {
        Thread aThread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "::start");
            while (!ready) {
            }
            System.out.println(Thread.currentThread().getName() + "::" + number);
        }, "A-thread");
        
        aThread.start();
        
        doTask(100L);
    
        number = 42;
        ready = true;
        System.out.println(Thread.currentThread().getName() + "::" + number);
        System.out.println(Thread.currentThread().getName() + "::" + ready);
    }
}