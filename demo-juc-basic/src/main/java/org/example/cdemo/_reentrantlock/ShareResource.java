package org.example.cdemo._reentrantlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ShareResource {
    private int flag = 1;
    private int day = 1;
    private final Lock lock = new ReentrantLock();
    private final Condition condition1 = lock.newCondition();
    private final Condition condition2 = lock.newCondition();
    private final Condition condition3 = lock.newCondition();
    
    public void doSomethingA(int loop) throws InterruptedException {
        lock.lock();
        try {
            while (flag != 1) {
                condition1.await();
            }
            System.out.printf("第%s天%n", day++);
            for (int i = 0; i < loop; i++) {
                System.out.println(Thread.currentThread().getName() + "::吃饭::" + i + "/" + loop);
            }
            Thread.sleep(2000);
            flag = 2;
            condition2.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public void doSomethingB(int loop) throws InterruptedException {
        lock.lock();
        try {
            while (flag != 2) {
                condition2.await();
            }
            for (int i = 0; i < loop; i++) {
                System.out.println(Thread.currentThread().getName() + "::睡觉::" + i + "/" + loop);
            }
            Thread.sleep(2000);
            flag = 3;
            condition3.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public void doSomethingC(int loop) throws InterruptedException {
        lock.lock();
        try {
            while (flag != 3) {
                condition3.await();
            }
            for (int i = 0; i < loop; i++) {
                System.out.println(Thread.currentThread().getName() + "::学习::" + i + "/" + loop);
            }
            Thread.sleep(2000);
            flag = 1;
            condition1.signal();
        } finally {
            lock.unlock();
        }
    }
}
