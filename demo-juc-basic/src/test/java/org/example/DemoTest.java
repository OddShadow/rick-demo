package org.example;

import org.junit.Test;

import java.util.Date;

public class DemoTest {
    
    @Test
    public void test01() {
        System.out.println("start=====");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("end=====");
    }
    
    @Test
    public void test02() {
        // java.lang.IllegalMonitorStateException: current thread is not owner
        System.out.println("start=====");
        Date date = new Date();
        try {
            date.wait(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("end=====");
    }
    
}
