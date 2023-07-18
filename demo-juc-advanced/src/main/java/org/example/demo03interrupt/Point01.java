package org.example.demo03interrupt;

import static org.example.utils.CpuUtils.doTask;

// 证明修改 线程中断状态=true 对线程毫无影响
public class Point01 {
    public static void main(String[] args) {
        Thread aThread = new Thread(() -> {
            while (true) {
                doTask(800L);
                System.out.println("====cyclic====");
            }
        }, "A");
        aThread.start();

        doTask(1200L);
        System.out.println("中断前值===" + aThread.isInterrupted());
        aThread.interrupt();
        System.out.println("中断后值===" + aThread.isInterrupted());
    }
}
