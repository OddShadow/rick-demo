package org.example.ademo._synchronized;

public class SynchronizedTicket {
    
    private static final long TOTAL_NUMBER = 20000L;
    public long number = TOTAL_NUMBER;
    
    public synchronized void sale() {
        if (number > 0) {
            System.out.println(Thread.currentThread().getName() + "-[sale:" + number-- + "]-remain-[" + number + "]");
        }
    }
    
}
