package org.example.f;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ShareResource {
    private int flag = 1;
    private final Lock lock = new ReentrantLock();
    private final Condition condition1 = lock.newCondition();
    private final Condition condition2 = lock.newCondition();
    private final Condition condition3 = lock.newCondition();
    
    public void print5(int loop) throws InterruptedException {
        lock.lock();
        try {
            while (flag != 1) {
                condition1.await();
            }
            for (int i = 0; i < 5; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i + "::" + loop);
            }
            flag = 2;
            condition2.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public void print10(int loop) throws InterruptedException {
        lock.lock();
        try {
            while (flag != 2) {
                condition2.await();
            }
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i + "::" + loop);
            }
            flag = 3;
            condition3.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public void print15(int loop) throws InterruptedException {
        lock.lock();
        try {
            while (flag != 3) {
                condition3.await();
            }
            for (int i = 0; i < 15; i++) {
                System.out.println(Thread.currentThread().getName() + "::" + i + "::" + loop);
            }
            flag = 1;
            condition1.signal();
        } finally {
            lock.unlock();
        }
    }
}
