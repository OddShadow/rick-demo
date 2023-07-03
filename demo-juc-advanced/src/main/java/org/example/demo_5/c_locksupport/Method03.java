package org.example.demo_5.c_locksupport;

import java.util.concurrent.locks.LockSupport;

import static org.example.utils.CpuUtils.doTask;

// LockSupport 完成线程同步
public class Method03 {
    public static void main(String[] args) {
        Thread aThread = new Thread(() -> {
            System.out.printf("%s::阻塞::%n", Thread.currentThread().getName());
            LockSupport.park();
            System.out.printf("%s::成功被唤醒::%n", Thread.currentThread().getName());
        }, "A-Thread");
        aThread.start();

        doTask(2);
        new Thread(() -> {
            LockSupport.unpark(aThread);
            System.out.printf("%s::唤醒线程::%n", Thread.currentThread().getName());
        }, "B-Thread").start();
    }
}
