package org.example.ademo._reentrantlock;

/**
 * 实现功能
 *    三个售票员合力卖出卖票2w张票
 * 用 ReentrantLock 实现，安全性得到保证，但是效率低，强制串行执行
 * 注意在 finally 释放锁
 */
public class ReentrantLockDemo {
    public static void main(String[] args) {
        
        ReentrantLockTicket reentrantLockTicket = new ReentrantLockTicket();
        
        new Thread(() -> {
            while (reentrantLockTicket.number != 0) {
                reentrantLockTicket.sale();
            }
        }, "A=Thread").start();
        
        new Thread(() -> {
            while (reentrantLockTicket.number != 0) {
                reentrantLockTicket.sale();
            }
        }, "B=Thread").start();
        
        new Thread(() -> {
            while (reentrantLockTicket.number != 0) {
                reentrantLockTicket.sale();
            }
        }, "C=Thread").start();
        
    }
}
