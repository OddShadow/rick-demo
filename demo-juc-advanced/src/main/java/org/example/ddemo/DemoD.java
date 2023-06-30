package org.example.ddemo;

import org.example.utils.CpuUtils;

public class DemoD {
    public static void main(String[] args) {
        Thread aThread = new Thread(() -> {
            for (int i = 0; i < 200; i++) {
                System.out.printf("=====%03d=====%n", i);
            }
            System.out.printf("3.aThread 查看-中断标识位:%s%n", Thread.currentThread().isInterrupted());
        });
        System.out.printf("1.默认-中断标识位:%s%n", Thread.currentThread().isInterrupted());
        aThread.start();
        CpuUtils.doTask(10L);
        aThread.interrupt(); // 【重要】
        System.out.printf("2.主线程第一次查看-中断标识位:%s%n", aThread.isInterrupted());
        CpuUtils.doTask(1000L);
        System.out.printf("4.主线程第二次查看-中断标识位:%s%n", aThread.isInterrupted());
    }
}
