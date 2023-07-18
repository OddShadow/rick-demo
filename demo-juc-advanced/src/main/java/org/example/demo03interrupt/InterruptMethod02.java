package org.example.demo03interrupt;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.example.utils.CpuUtils.doTask;

// 使用 Atomic 变量中断协商
public class InterruptMethod02 {
    private static final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                if (atomicBoolean.get()) {
                    System.out.println("end===");
                    break;
                }
                System.out.println("ing---");
            }
        }, "A").start();
        doTask(1000L);
        atomicBoolean.set(true);
    }
}
