package org.example.demo_14;

public class MyReentrantReadWriteLock03 {
    public static void main(String[] args) {
        // ReadWriteLock 支持并发读
        MyResource01 myResource = new MyResource01();
    
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                myResource.rwWrite(finalI + "", finalI + "");
            }, i + "-Thread").start();
        }
    
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                myResource.rwRead("init");
            }, i + "-Thread").start();
        }
    }
}
