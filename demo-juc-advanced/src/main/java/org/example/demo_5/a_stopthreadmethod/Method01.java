package org.example.demo_5.a_stopthreadmethod;

import static org.example.utils.CpuUtils.doTask;

public class Method01 {

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
