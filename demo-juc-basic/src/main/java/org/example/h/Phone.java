package org.example.h;

public class Phone {
    
    public synchronized void sendSms() {
        System.out.println("=====sendSms=====");
    }
    
    public synchronized void sendSmsEmail() {
        System.out.println("=====sendSmsEmail=====");
    }
    
    public void getHello() {
        System.out.println("=====getHello=====");

    }
}

