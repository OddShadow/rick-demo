package org.example.demo03interrupt;

import static org.example.utils.CpuUtils.doTask;

// 使用 volatile 变量中断协商
public class InterruptMethod01 {
    private static volatile boolean isStop = false;
    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                if (isStop) {
                    System.out.println("end===");
                    break;
                }
                System.out.println("ing---");
            }
        }, "A").start();
        doTask(1000L);
        isStop = true;
    }
}
