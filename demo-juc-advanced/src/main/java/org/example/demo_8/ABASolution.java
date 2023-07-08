package org.example.demo_8;

import org.example.utils.CpuUtils;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

public class ABASolution {
    public static void main(String[] args) {
        problem();
        System.out.println("=========");
        solution1();
        System.out.println("=========");
        System.out.println(1==1);
        System.out.println(500==500);
        System.out.println(Integer.valueOf(1) == Integer.valueOf(1));
        System.out.println(Integer.valueOf(500)==Integer.valueOf(500));
        
    }
    
    private static void problem() {
        AtomicReference<Integer> atomicReference = new AtomicReference<>(100);
        new Thread(() -> {
            atomicReference.compareAndSet(new Integer(100), new Integer(200));
            System.out.println("1::" + atomicReference.get());
            atomicReference.compareAndSet(new Integer(200), new Integer(100));
            System.out.println("2::" + atomicReference.get());
        }).start();
        CpuUtils.doTask(20);
        new Thread(() -> {
            atomicReference.compareAndSet(new Integer(100), new Integer(666));
            System.out.println("3::" + atomicReference.get());
        }).start();
        CpuUtils.doTask(20);
        System.out.println(atomicReference.get());
    }
    
    private static void solution1() {
        AtomicStampedReference<Integer> atomicReference = new AtomicStampedReference<>(0, 1);
        new Thread(() -> {
            atomicReference.compareAndSet(0, 1, 1, 2);
            System.out.println("0 --> 1");
            atomicReference.compareAndSet(1, 2, 2, 3);
            System.out.println("1 --> 2");
            atomicReference.compareAndSet(2, 0, 3, 4);
            System.out.println("2 --> 0");
        }).start();
        CpuUtils.doTask(20);
        new Thread(() -> {
            atomicReference.compareAndSet(0, 99, 1, 2);
            System.out.println("0 --> 99");
        }).start();
        CpuUtils.doTask(50);
        System.out.println(atomicReference.get(new int[]{1, 2, 3, 4}));
    }
    
    private static void solution2() {
        AtomicStampedReference<Integer> atomicReference = new AtomicStampedReference<>(0, 1);
        new Thread(() -> {
            atomicReference.compareAndSet(0, 1, 1, 2);
            System.out.println("0 --> 1");
            atomicReference.compareAndSet(1, 2, 2, 3);
            System.out.println("1 --> 2");
            atomicReference.compareAndSet(2, 0, 3, 4);
            System.out.println("2 --> 0");
        }).start();
        CpuUtils.doTask(20);
        new Thread(() -> {
            atomicReference.compareAndSet(0, 99, 1, 2);
            System.out.println("0 --> 99");
        }).start();
        CpuUtils.doTask(50);
        System.out.println(atomicReference.get(new int[]{1, 2, 3, 4}));
    }
}
