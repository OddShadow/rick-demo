package org.example.demo_9.fieldupdater;

import java.util.concurrent.CountDownLatch;

public class AtomicFieldUpdater01 {
    public static void main(String[] args) throws InterruptedException {
        Account account = new Account();
        CountDownLatch countDownLatch = new CountDownLatch(100);
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                try {
                    for (int j = 0; j < 10; j++) {
                        account.addMoney(account);
                    }
                } finally {
                    countDownLatch.countDown();
                }
            }, i + "::Thread").start();
        }
        countDownLatch.await();
        System.out.println(account.money);
    }
}