package org.example.demo_5.a_stopthreadmethod;

import static org.example.utils.CpuUtils.doTask;

public class Method03 {
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
