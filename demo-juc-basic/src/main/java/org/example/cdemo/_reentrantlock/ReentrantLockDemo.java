package org.example.cdemo._reentrantlock;

/**
 * 线程间定制化通信
 * 实现功能
 *    每天 吃3顿饭 睡2次觉 学1次习
 */
public class ReentrantLockDemo {
    public static void main(String[] args) {
        
        ShareResource shareResource = new ShareResource();
        
        new Thread(() -> {
            while (true) {
                try {
                    shareResource.doSomethingA(3);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "AAA").start();
        
        new Thread(() -> {
            while (true) {
                try {
                    shareResource.doSomethingB(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "BBB").start();
        
        new Thread(() -> {
            while (true) {
                try {
                    shareResource.doSomethingC(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "CCC").start();
        
    }
}
