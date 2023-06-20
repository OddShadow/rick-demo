package org.example.bdemo._synchronized;

class SynchronizedShare {
    
    private int number = 0;
    
    public synchronized void incr() throws InterruptedException {
        // 防止虚假唤醒
        while (number != 0) {
            this.wait();
        }
        number++;
        System.out.println(Thread.currentThread().getName() + "::" + number);
        this.notifyAll();
    }
    
    public synchronized void decr() throws InterruptedException {
        // 防止虚假唤醒
        while (number != 1) {
            this.wait();
        }
        number--;
        System.out.println(Thread.currentThread().getName() + "::" + number);
        this.notifyAll();
    }
    
}
