package org.example.demo_6.b_important;

public class Point04 {
    public static void main(String[] args) {
        System.out.println("初始值::" + Thread.currentThread().isInterrupted());
        Thread.currentThread().interrupt();
        System.out.println("仅获取1::" + Thread.currentThread().isInterrupted());
        System.out.println("仅获取2::" + Thread.currentThread().isInterrupted());
        System.out.println("静态方法获取1::" + Thread.interrupted()); // 返回标志位，并清除
        System.out.println("静态方法获取2::" + Thread.interrupted());
    }
}
