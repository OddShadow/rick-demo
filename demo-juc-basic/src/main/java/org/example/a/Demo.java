package org.example.a;

/**
 * 写一个程序完成 车票 从 30 卖到 0
 */
public class Demo {
    
    /**
     * 多线程编程步骤
     * 1. 创建资源类
     * 2. 创建多个线程，调用资源类的方法
     */
    public static void main(String[] args) {
        Ticket ticket = new Ticket();
        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "A=Thread").start();
        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "B=Thread").start();
        new Thread(() -> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        }, "C=Thread").start();
    }
    
}
