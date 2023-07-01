package org.example.demo_6.b_important;

import static org.example.utils.CpuUtils.doTask;

// 证明一下修改设置 线程中断状态=true 对线程毫无影响
public class Point01 {
    public static void main(String[] args) {
        Thread aThread = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                System.out.println(i + "::ing::" + Thread.currentThread().isInterrupted());
            }
            System.out.println("===end===");
        }, "A");
        aThread.start();

        System.out.println("初始值===" + aThread.isInterrupted());
        doTask(1);
        aThread.interrupt();
        System.out.println("中断后值===" + aThread.isInterrupted());
    }
}
