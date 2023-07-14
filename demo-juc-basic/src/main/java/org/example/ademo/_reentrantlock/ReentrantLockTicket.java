package org.example.ademo._reentrantlock;

import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockTicket {
    
    private static final long TOTAL_NUMBER = 20000L;
    public long number = TOTAL_NUMBER;
    
    private final ReentrantLock lock = new ReentrantLock();
    
    public void sale() {
        lock.lock();
        try {
            if (number > 0) {
                System.out.println(Thread.currentThread().getName() + "-[sale:" + number-- + "]-remain-[" + number + "]");
            }
        } finally {
            lock.unlock();
        }
    }
    
}
