package org.example.a;

public class Ticket {
    private int number = 30;
    public synchronized void sale() {
        if (number > 0) {
            System.out.println(Thread.currentThread().getName() + "-[sale:" + number-- + "]-remain-[" + number + "]");
        }
    }
}
