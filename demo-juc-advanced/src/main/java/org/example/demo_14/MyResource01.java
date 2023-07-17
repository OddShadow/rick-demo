package org.example.demo_14;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class MyResource01 {
    static Map<String, String> buffer = new HashMap<>();
    
    static {
        buffer.put("init", "Good");
    }
    
    Lock reentrantLock = new ReentrantLock();
    ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    
    public void write(String key, String value) {
        reentrantLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "::write::key=" + key + "::value=" + value);
            buffer.put(key, value);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "::write::over");
        } finally {
            reentrantLock.unlock();
        }
    }
    
    public void read(String key) {
        reentrantLock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "::read::" + buffer.get(key));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } finally {
            reentrantLock.unlock();
        }
    }
    
    public void rwWrite(String key, String value) {
        readWriteLock.writeLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "::write::key=" + key + "::value=" + value);
            buffer.put(key, value);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName() + "::write::over");
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
    
    public void rwRead(String key) {
        readWriteLock.readLock().lock();
        try {
            System.out.println(Thread.currentThread().getName() + "::read::" + buffer.get(key));
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } finally {
            readWriteLock.readLock().unlock();
        }
    }
}
