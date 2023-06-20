package org.example.ddemo.summary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

public class SummaryDemo {
    public static void main(String[] args) {
    
        List<Object> list = new ArrayList<>(); // 线程不安全
        Vector<Object> vector = new Vector<>(); // 线程安全
        CopyOnWriteArrayList<Object> copyOnWriteArrayList = new CopyOnWriteArrayList<>(); // 线程安全
        
        Set<Object> set = new HashSet<>(); // 线程不安全
        CopyOnWriteArraySet<Object> copyOnWriteArraySet = new CopyOnWriteArraySet<>(); // 线程安全
    
        Map<Object, Object> hashMap = new HashMap<>(); // 线程不安全
        ConcurrentHashMap<Object, Object> concurrentHashMap = new ConcurrentHashMap<>(); // 线程安全
    
    
    }
}
