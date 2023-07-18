package org.example.demo03interrupt;

//  Thread.interrupted() 判断线程是否被中断 并 清除当前中断状态
public class Point04 {
    public static void main(String[] args) {
        Thread.currentThread().interrupt();
        System.out.println("静态方法获取1::" + Thread.interrupted()); // 返回标志位，并清除
        System.out.println("静态方法获取2::" + Thread.interrupted());
    }
}
