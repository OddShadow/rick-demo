package org.example.demo_5.c_locksupport;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.example.utils.CpuUtils.doTask;

// reentrantLock 完成线程同步
public class Method02 {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(() -> {
            lock.lock();
            try {
                System.out.printf("%s::阻塞::%n", Thread.currentThread().getName());
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
            System.out.printf("%s::成功被唤醒::%n", Thread.currentThread().getName());
        }, "A-Thread").start();

        doTask(2);
        new Thread(() -> {
            lock.lock();
            try {
                System.out.printf("%s::唤醒线程::%n", Thread.currentThread().getName());
                condition.signal();
            } finally {
                lock.unlock();
            }
        }, "B-Thread").start();
    }
}
