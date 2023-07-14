package org.example.ddemo;

import java.util.concurrent.TimeUnit;

public class DemoG {
    public static void main(String[] args) {
        Object lock = new Object();
        
        new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    System.out.println("你拍一");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "A-Thread");
    
        new Thread(() -> {
            while (true) {
                synchronized (lock) {
                    System.out.println("你拍二");
                    try {
                        TimeUnit.SECONDS.sleep(1);
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }, "B-Thread");
    }
}
