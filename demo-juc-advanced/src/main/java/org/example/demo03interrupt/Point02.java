package org.example.demo03interrupt;

import static org.example.utils.CpuUtils.doTask;

// 中断不活动的线程不会产生任何影响，返回 false
public class Point02 {
    public static void main(String[] args) {
        Thread aThread = new Thread(() -> {
            doTask(5000L);
        }, "A-Thread");

        System.out.println("准备中断");
        aThread.interrupt(); // 毫无影响
    
        System.out.println("flag=" + aThread.isInterrupted());
        aThread.start();
        System.out.println("flag=" + aThread.isInterrupted());
    }
}
