package org.example.g;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * ArrayList HashSet HashMap 是线程不安全的
 * 解决方案
 *   1. 使用 List<String> list = new Vector<>();
 *   2. 使用 List<String> list = Collections.synchronizedList(new ArrayList<>());
 *   3. 使用 List<String> list = new CopyOnWriteArrayList<>(); 写时复制技术
 */
public class Demo {
    public static void main(String[] args) {
//        test01();
//        test02();
        test03();
    }
    
    public static void test01() {
        // Exception in thread "68" java.util.ConcurrentModificationException
//        List<String> list = new ArrayList<>();
        List<String> list = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                list.add(UUID.randomUUID().toString().substring(3));
                System.out.println(list);
            }, String.valueOf(i)).start();
        }
    }
    
    public static void test02() {
        // Exception in thread "68" java.util.ConcurrentModificationException
//        Set<String> set = new HashSet<>();
        Set<String> set = new CopyOnWriteArraySet<>();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                set.add(UUID.randomUUID().toString().substring(3));
                System.out.println(set);
            }, String.valueOf(i)).start();
        }
    }
    
    public static void test03() {
        // Exception in thread "68" java.util.ConcurrentModificationException
//        Map<String, String> map = new HashMap<>();
        Map<String, String> map = new ConcurrentHashMap<>();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                map.put(UUID.randomUUID().toString().substring(3), UUID.randomUUID().toString().substring(3));
                System.out.println(map);
            }, String.valueOf(i)).start();
        }
    }
}
