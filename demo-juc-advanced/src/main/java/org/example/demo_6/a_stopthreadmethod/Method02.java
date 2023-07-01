package org.example.demo_6.a_stopthreadmethod;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.example.utils.CpuUtils.doTask;

public class Method02 {

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
