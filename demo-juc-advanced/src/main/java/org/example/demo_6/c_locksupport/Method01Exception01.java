package org.example.demo_6.c_locksupport;

import static org.example.utils.CpuUtils.doTask;

// 必须 synchronized 修饰，否则 java.lang.IllegalMonitorStateException
public class Method01Exception01 {
    public static void main(String[] args) {
        Object obj = new Object();
        new Thread(() -> {
//          synchronized (obj) {
            System.out.printf("%s::阻塞::%n", Thread.currentThread().getName());
            try {
                obj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("%s::成功被唤醒::%n", Thread.currentThread().getName());
//          }
        }, "A-Thread").start();

        doTask(2);
        new Thread(() -> {
//          synchronized (obj) {
            System.out.printf("%s::唤醒线程::%n", Thread.currentThread().getName());
            obj.notify();
//          }
        }, "B-Thread").start();
    }
}
