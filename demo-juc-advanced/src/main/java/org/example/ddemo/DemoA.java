package org.example.ddemo;

import org.example.utils.CpuUtils;

public class DemoA {
    
    private static volatile boolean flag = false;
    
    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                if (flag) {
                    System.out.println("标志位被修改，终止");
                    break;
                }
                System.out.println("循环中----");
            }
        }, "A-Thread").start();
    
        CpuUtils.doTask(10L);
        
        new Thread(() -> flag = true, "B-Thread").start();
    }
}
