package org.example.demo_9;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicAtomic {
    private static final AtomicInteger atomicInteger = new AtomicInteger();
    public static void main(String[] args) throws InterruptedException {
        int size = 50;
        CountDownLatch countDownLatch = new CountDownLatch(size);
        for (int i = 0; i < size; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < 1000; j++) {
                        atomicInteger.getAndIncrement();
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }, String.format("My-%s-Thread", i)).start();
        }
    
        countDownLatch.await();
        System.out.println(atomicInteger.get());
    }
}
