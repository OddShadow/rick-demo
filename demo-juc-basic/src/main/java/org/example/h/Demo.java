package org.example.h;

public class Demo {
    
    public static void main(String[] args) throws InterruptedException {
        Phone phone = new Phone();
        
        new Thread(() -> {
            try {
                phone.sendSms();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, "A-Thread").start();
    
        Thread.sleep(100);
        
        new Thread(() -> {
            try {
                phone.sendSmsEmail();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, "B-Thread").start();
    }
    
}
