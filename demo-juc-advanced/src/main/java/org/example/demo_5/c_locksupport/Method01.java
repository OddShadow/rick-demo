package org.example.demo_5.c_locksupport;

import static org.example.utils.CpuUtils.doTask;

// object 完成线程同步
public class Method01 {
    public static void main(String[] args) {
        Object obj = new Object();
        new Thread(() -> {
            synchronized (obj) {
                System.out.printf("%s::阻塞::%n", Thread.currentThread().getName());
                try {
                    obj.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.printf("%s::成功被唤醒::%n", Thread.currentThread().getName());
            }
        }, "A-Thread").start();

        doTask(2);
        new Thread(() -> {
            synchronized (obj) {
                System.out.printf("%s::唤醒线程::%n", Thread.currentThread().getName());
                obj.notify();
            }
        }, "B-Thread").start();
    }
}
