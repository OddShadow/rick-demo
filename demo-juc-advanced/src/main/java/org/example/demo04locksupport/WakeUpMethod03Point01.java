package org.example.demo04locksupport;

import java.util.concurrent.locks.LockSupport;

import static org.example.utils.CpuUtils.doTask;

// 无需同步代码，无需考虑 休眠方法 和 唤醒方法 的先后顺序
// unpark() 发放许可证需要 thread 存在
public class WakeUpMethod03Point01 {
    public static void main(String[] args) {
        Thread aThread = new Thread(() -> {
            doTask(1000);
            System.out.printf("%s::阻塞::%n", Thread.currentThread().getName());
            LockSupport.park();
            System.out.printf("%s::成功被唤醒::%n", Thread.currentThread().getName());
        }, "A-Thread");

        Thread bThread = new Thread(() -> {
            LockSupport.unpark(aThread);
            System.out.printf("%s::唤醒线程::%n", Thread.currentThread().getName());
        }, "B-Thread");

        bThread.start();
        aThread.start();
    }
}
