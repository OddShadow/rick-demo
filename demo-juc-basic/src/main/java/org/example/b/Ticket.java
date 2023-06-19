package org.example.b;

import java.util.concurrent.locks.ReentrantLock;

public class Ticket {
    private int number = 30;
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
