package org.example.neww;


import java.time.Instant;

// java.time.Instant
public class NewInstant {
    public static void main(String[] args) {
        log();
    }
    
    private static void test() {
        Instant now = Instant.now(); // 构造对象 1970-01-01 00:00:00 到现在的总秒数 + 不足1秒的 nm 数
        long epochSecond = now.getEpochSecond(); // 1970-01-01 00:00:00 到现在的总秒数
        int nano = now.getNano(); // 不足1秒的 nm 数
    
        Instant instant1 = now.plusSeconds(1);// 加上 1s
        Instant instant2 = now.minusSeconds(1);
    }
    
    private static void log() {
        // 取决于计算机精度
        Instant instant1 = Instant.now();
        for (int i = 0; i < 100; i++) {
            System.out.println("666");
        }
        Instant instant2 = Instant.now();
        System.out.println("exec cost time: " + (instant2.getNano() - instant1.getNano()) + "ns");
    }
}
