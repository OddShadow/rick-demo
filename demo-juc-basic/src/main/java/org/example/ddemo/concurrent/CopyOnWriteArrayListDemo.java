package org.example.ddemo.concurrent;

import java.util.concurrent.CopyOnWriteArrayList;

public class CopyOnWriteArrayListDemo {
    public static void main(String[] args) {
        
        // 可以看一下 add 源码 - 写时复制技术
        CopyOnWriteArrayList<Object> list = new CopyOnWriteArrayList<>();
    
        new Thread(() -> {
            while (true) {
                list.add(0);
                System.out.println(list);
            }
        }, "A-Thread").start();
    
        new Thread(() -> {
            while (true) {
                list.remove(list.size() - 1);
                System.out.println(list);
            }
        }, "B-Thread").start();
        
    }
}
