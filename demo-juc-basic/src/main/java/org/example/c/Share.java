package org.example.c;

class Share {
    private int number = 0;
    // 在资源类操作方法 1.判断 2.干活 3.通知
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
