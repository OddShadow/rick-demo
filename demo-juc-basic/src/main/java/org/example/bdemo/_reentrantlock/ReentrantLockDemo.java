package org.example.bdemo._reentrantlock;

/**
 * 实现功能
 *    A-打印1 B-打印0  交替执行各10次
 */
public class ReentrantLockDemo {
    public static void main(String[] args) {
        
        ReentrantLockShare reentrantLockShare = new ReentrantLockShare();
        
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    reentrantLockShare.incr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "A=Thread").start();
        
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    reentrantLockShare.decr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "B=Thread").start();
        
    }
}
