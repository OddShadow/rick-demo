package org.example.cdemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.example.utils.CpuUtils.doTask;

/*
    对计算结果合并
        1. thenCombine()
 */
public class CompletableFutureApi05 {
    
    private static final ExecutorService executors = Executors.newFixedThreadPool(6);
    
    public static void main(String[] args) {
        CompletableFuture<Long> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            long l = doTask(88L);
            System.out.printf("%-30s completableFuture1 得到结果%d%n", Thread.currentThread().getName(), l);
            return l;
        }, executors);
        CompletableFuture<Long> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            long l = doTask(66L);
            System.out.printf("%-30s completableFuture2 得到结果%d%n", Thread.currentThread().getName(), l);
            return l;
        }, executors);
        
        CompletableFuture<Long> completableFuture = completableFuture1.thenCombine(completableFuture2, (x, y) -> {
            System.out.printf("%-30s得到合并结果%d%n", Thread.currentThread().getName(), x + y);
            return x + y;
        });
        System.out.println(completableFuture.join());
    
        executors.shutdown();
    }
}
