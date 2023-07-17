package org.example.demo_13_aqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// https://raw.githubusercontent.com/OddShadow/images/main/rick-demo/202307172250145.png
// 自己 debug 调试
public class MyDemo {
    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "::lock");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } finally {
                System.out.println(Thread.currentThread().getName() + "::unlock");
                lock.unlock();
            }
        }, "A-Thread").start();
    
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "::lock");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } finally {
                System.out.println(Thread.currentThread().getName() + "::unlock");
                lock.unlock();
            }
        }, "B-Thread").start();
    
        new Thread(() -> {
            lock.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "::lock");
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } finally {
                System.out.println(Thread.currentThread().getName() + "::unlock");
                lock.unlock();
            }
        }, "C-Thread").start();
    }
}
