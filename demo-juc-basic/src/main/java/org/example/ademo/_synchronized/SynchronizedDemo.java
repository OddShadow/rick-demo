package org.example.ademo._synchronized;

/**
 * 实现功能
 *    三个售票员合力卖出卖票2w张票
 * 用 synchronized 实现，安全性得到保证，但是效率低，强制串行执行
 */
public class SynchronizedDemo {
    public static void main(String[] args) {
        
        SynchronizedTicket synchronizedTicket = new SynchronizedTicket();
        
        new Thread(() -> {
            while (synchronizedTicket.number != 0) {
                synchronizedTicket.sale();
            }
        }, "A=Thread").start();
        
        new Thread(() -> {
            while (synchronizedTicket.number != 0) {
                synchronizedTicket.sale();
            }
        }, "B=Thread").start();
        
        new Thread(() -> {
            while (synchronizedTicket.number != 0) {
                synchronizedTicket.sale();
            }
        }, "C=Thread").start();
        
    }
}
