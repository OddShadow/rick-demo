package org.example.demo_7;

import org.example.utils.CpuUtils;

public class Visibility {
    private static boolean ready;
    private static int number;
//    private static volatile boolean ready;
//    private static volatile int number;
    public static void main(String[] args) {
        Thread aThread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "::start");
            while (!ready) {
            }
            System.out.println(Thread.currentThread().getName() + "::" + number);
        }, "A-thread");
        
        aThread.start();
        
        CpuUtils.doTask(100L);
    
        number = 42;
        ready = true;
        System.out.println(Thread.currentThread().getName() + "::" + number);
        System.out.println(Thread.currentThread().getName() + "::" + ready);
    }
}