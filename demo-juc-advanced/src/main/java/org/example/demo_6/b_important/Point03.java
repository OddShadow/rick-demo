package org.example.demo_6.b_important;

import static org.example.utils.CpuUtils.doTask;

public class Point03 {
    public static void main(String[] args) {
        Thread aThread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println(Thread.currentThread().getName() + "::break::" + Thread.currentThread().isInterrupted());
                    break;
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // 这里不调用则会进入死循环
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "::end");
            }
        }, "A-Thread");
        aThread.start();

        doTask(1000);
        // java.lang.InterruptedException: sleep interrupted
        aThread.interrupt();
    }
}
