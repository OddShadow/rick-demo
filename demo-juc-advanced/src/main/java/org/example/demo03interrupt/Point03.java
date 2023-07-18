package org.example.demo03interrupt;

import java.util.concurrent.TimeUnit;

import static org.example.utils.CpuUtils.doTask;

public class Point03 {
    public static void main(String[] args) {
        Thread aThread = new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 这里不调用则会进入死循环
                    e.printStackTrace();
                }
                System.out.println("go");
            }
        }, "A-Thread");
        aThread.start();

        doTask(1000);
        // java.lang.InterruptedException: sleep interrupted
        aThread.interrupt();
    }
}
