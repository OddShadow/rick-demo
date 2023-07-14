package org.example.ddemo.error;

import java.util.ArrayList;
import java.util.List;

public class ArrayListDemo {
    public static void main(String[] args) {
    
        List<Integer> list = new ArrayList<>();
    
        // java.util.ConcurrentModificationException
        new Thread(() -> {
            while (true) {
                list.add(0);
                System.out.println(list);
            }
        }, "A-Thread").start();
        
        // java.lang.IndexOutOfBoundsException
        new Thread(() -> {
            while (true) {
                list.remove(list.size() - 1);
                System.out.println(list);
            }
        }, "B-Thread").start();
    
    }
}
