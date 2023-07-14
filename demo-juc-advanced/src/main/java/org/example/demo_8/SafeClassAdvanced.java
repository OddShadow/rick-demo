package org.example.demo_8;

import java.util.concurrent.atomic.AtomicInteger;

public class SafeClassAdvanced {
    private final AtomicInteger atomicInteger = new AtomicInteger(0);
    public int getAtomicInteger() {
        return atomicInteger.get();
    }
    public void setAtomicInteger() {
        atomicInteger.getAndIncrement();
    }
    public void addAtomicInteger(int number) {
        atomicInteger.getAndAdd(number);
    }
    // unsafe noAtomic
    public void updateAtomicInteger(int number) {
        int i = atomicInteger.get();
        atomicInteger.compareAndSet(i, i + number);
    }
    
    public static void main(String[] args) {
        SafeClassAdvanced safeClassAdvanced = new SafeClassAdvanced();
        for (int i = 0; i < 1000; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
//                    safeClassAdvanced.setAtomicInteger();
//                    safeClassAdvanced.addAtomicInteger(2);
                    safeClassAdvanced.updateAtomicInteger(1);
                }
            }).start();
        }
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(safeClassAdvanced.getAtomicInteger());
    }
}
