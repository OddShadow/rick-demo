package org.example.demo_6.c_locksupport;

import java.util.concurrent.locks.LockSupport;

import static org.example.utils.CpuUtils.doTask;

// permit 累计上限只有1，每次调用 park() 方法会消耗掉 permit
public class Method03Point02 {
    public static void main(String[] args) {
        Thread aThread = new Thread(() -> {
            doTask(1000);
            System.out.printf("%s::阻塞::%n", Thread.currentThread().getName());
            LockSupport.park();
            LockSupport.park();
            System.out.printf("%s::成功被唤醒::%n", Thread.currentThread().getName());
        }, "A-Thread");

        Thread bThread = new Thread(() -> {
            LockSupport.unpark(aThread);
            LockSupport.unpark(aThread);
            System.out.printf("%s::唤醒线程::%n", Thread.currentThread().getName());
        }, "B-Thread");

        bThread.start();
        aThread.start();
    }
}
