package org.example.ddemo;

public class DemoF {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName() + "::" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "::" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "::" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "::" + Thread.interrupted());
        System.out.println("first");
        Thread.currentThread().interrupt();
        System.out.println("second");
        System.out.println(Thread.currentThread().getName() + "::" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "::" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "::" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "::" + Thread.interrupted());
        System.out.println(Thread.currentThread().getName() + "::" + Thread.interrupted());
    }
}
