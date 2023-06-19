package org.example.c;

/**
 * synchronized 写一个成程序完成 A-0 B-1 交替执行各10次数
 */
public class Demo {
    
    public static void main(String[] args) {
        Share share = new Share();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.incr();
//                    System.out.println(Thread.currentThread().getName() + "==>" + i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "A=Thread").start();
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    share.decr();
//                    System.out.println(Thread.currentThread().getName() + "==>" + i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "B=Thread").start();
    }

}
