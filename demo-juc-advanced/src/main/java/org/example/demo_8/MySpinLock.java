package org.example.demo_8;

import org.example.utils.CpuUtils;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class MySpinLock {
    AtomicReference<Thread> atomicReference = new AtomicReference<>();
    public void lock() {
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName() + "::lock");
        while (!atomicReference.compareAndSet(null, thread)) {
        
        }
    }
    public void unLock() {
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread, null);
        System.out.println(thread.getName() + "::unLock");
    }
    public static void main(String[] args) {
        MySpinLock mySpinLock = new MySpinLock();
        new Thread(() -> {
            mySpinLock.lock();
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            mySpinLock.unLock();
        }, "A-Thread").start();
    
        CpuUtils.doTask(10);
        
        new Thread(() -> {
            mySpinLock.lock();
            mySpinLock.unLock();
        }, "B-Thread").start();
    }
}
