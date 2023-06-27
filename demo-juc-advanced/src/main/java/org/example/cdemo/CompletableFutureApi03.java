package org.example.cdemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
    对返回结果消费
        1. thenRun()
        2. thenAccept()
        3. thenApply()
 */
public class CompletableFutureApi03 {
    
    private static final ExecutorService executors = Executors.newFixedThreadPool(6);
    
    public static void main(String[] args) {
        CompletableFuture
                .supplyAsync(() -> 88)
                .thenRun(() -> {})
                .join();
        
        CompletableFuture
                .supplyAsync(() -> 88)
                .thenAccept(x -> {})
                .join();
        
        CompletableFuture
                .supplyAsync(() -> 88)
                .thenApply(x -> x);
    }
}
