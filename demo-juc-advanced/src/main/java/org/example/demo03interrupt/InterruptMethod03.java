package org.example.demo03interrupt;

import static org.example.utils.CpuUtils.doTask;

// 使用 线程自身中断标识位 中断协商
public class InterruptMethod03 {
    public static void main(String[] args) {
        Thread aThread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("end===");
                    break;
                }
                System.out.println("ing---");
            }
        }, "A");
        aThread.start();
        doTask(1000L);
        aThread.interrupt();
    }
}
