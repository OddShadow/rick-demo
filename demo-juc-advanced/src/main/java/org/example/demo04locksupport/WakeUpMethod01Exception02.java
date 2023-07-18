package org.example.demo04locksupport;

import static org.example.utils.CpuUtils.doTask;

public class WakeUpMethod01Exception02 {
    public static void main(String[] args) {
        Object obj = new Object();
        new Thread(() -> {
            doTask(800); // notify 比 wait 先调用, 程序无限休眠
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
