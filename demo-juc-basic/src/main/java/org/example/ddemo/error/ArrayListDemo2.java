package org.example.ddemo.error;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayListDemo2 {
    public static void main(String[] args) {
        
        List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);

        Iterator<Integer> it = list.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
            it.remove();
        }

        System.out.println("===执行成功===");

        List<Integer> list2 = new ArrayList<>();
        list2.add(1);
        list2.add(2);
        list2.add(3);

        for (Integer integer : list2) {
            System.out.println(integer);
            // java.util.ConcurrentModificationException
            list2.remove(0);
        }
        
    }
}
