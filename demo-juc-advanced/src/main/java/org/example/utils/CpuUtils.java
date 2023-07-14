package org.example.utils;

public class CpuUtils {
    
    // 让 cpu 忙一会
    public static long doTask(long times) {
        long sum = 0;
        for (long i = 0; i < times; i++) {
            for (long j = 0; j < times; j++) {
                for (long k = 0; k < times; k++) {
                    sum += k;
                }
            }
        }
        return sum;
    }
    
}
