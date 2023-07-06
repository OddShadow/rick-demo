package org.example.demo_8;

public class SafeClass {
    private volatile int number = 0;
    public int getNumber() {
        return number;
    }
    public synchronized void setNumber() {
        number++;
    }
    
    public static void main(String[] args) {
        SafeClass safeClass = new SafeClass();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    safeClass.setNumber();
                }
            }).start();
        }
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(safeClass.getNumber());
    }
}
