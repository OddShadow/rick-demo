package org.example.ddemo.sync;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class VectorDemo {
    public static void main(String[] args) {
    
        List<Object> list = Collections.synchronizedList(new ArrayList<>());
        Vector<Integer> vector = new Vector<>();
        
        new Thread(() -> {
            while (true) {
                vector.add(0);
                System.out.println(vector);
            }
        }, "A-Thread").start();
        
        new Thread(() -> {
            while (true) {
                vector.remove(vector.size() - 1);
                System.out.println(vector);
            }
        }, "B-Thread").start();
        
    }
}
