package org.example.ddemo;

import org.example.utils.CpuUtils;

import java.util.concurrent.atomic.AtomicBoolean;

public class DemoB {
    
    private static final AtomicBoolean flag = new AtomicBoolean(false);
    
    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                if (flag.get()) {
                    System.out.println("标志位被修改，终止");
                    break;
                }
                System.out.println("循环中----");
            }
        }, "A-Thread").start();
    
        CpuUtils.doTask(10L);
    
        new Thread(() -> flag.set(true), "B-Thread").start();
    }
}
