package org.example.bdemo._synchronized;

/**
 * 实现功能
 *    A-打印1 B-打印0  交替执行各10次
 */
public class SynchronizedDemo {
    public static void main(String[] args) {
        
        SynchronizedShare synchronizedShare = new SynchronizedShare();
        
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    synchronizedShare.incr();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "A=Thread").start();
        
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    synchronizedShare.decr();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "B=Thread").start();
        
    }
}
