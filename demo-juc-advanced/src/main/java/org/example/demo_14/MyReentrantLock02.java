package org.example.demo_14;

public class MyReentrantLock02 {
    public static void main(String[] args) {
        // 普通 ReentrantLock 无法并发读
        MyResource01 myResource = new MyResource01();
    
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            new Thread(() -> {
                myResource.write(finalI + "", finalI + "");
            }, i + "-Thread").start();
        }
    
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                myResource.read("init");
            }, i + "-Thread").start();
        }
    }
}
