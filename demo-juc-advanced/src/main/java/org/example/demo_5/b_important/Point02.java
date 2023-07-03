package org.example.demo_5.b_important;

import static org.example.utils.CpuUtils.doTask;

public class Point02 {
    public static void main(String[] args) {
        Thread aThread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i + "::ing::" + Thread.currentThread().isInterrupted());
            }
            System.out.println(Thread.currentThread().getName() + "::end::" + Thread.currentThread().isInterrupted());
        }, "A-Thread");
        aThread.start();

        System.out.println(Thread.currentThread().getName() + "::初始值::" + aThread.isInterrupted());
        doTask(1);
        aThread.interrupt();
        System.out.println(Thread.currentThread().getName() + "::中断后值::" + aThread.isInterrupted());

        doTask(1000);
        // 中断不活动的线程不会产生任何影响，返回 false
        System.out.println(Thread.currentThread().getName() + "::end::" + aThread.isInterrupted());
    }
}
