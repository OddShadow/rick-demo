package org.example.ddemo;

import org.example.utils.CpuUtils;

public class DemoE {
    public static void main(String[] args) {

        Thread aThread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("==> 被中断");
                    break;
                }
                System.out.println("==============aThread 执行中");
    
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    e.printStackTrace();
                }
            }
        });
        aThread.start();
        CpuUtils.doTask(10L);
        aThread.interrupt();
    }
    
}
