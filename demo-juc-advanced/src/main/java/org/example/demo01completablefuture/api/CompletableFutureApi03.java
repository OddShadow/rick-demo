package org.example.demo01completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*
    对返回结果消费
        1. thenRun()
        2. thenAccept()
        3. thenApply()
 */
public class CompletableFutureApi03 {
    public static void main(String[] args) {
    
        CompletableFuture
                .supplyAsync(() -> 88)
                .thenRun(() -> System.out.println("go!"));
        
        CompletableFuture
                .supplyAsync(() -> 88)
                .thenAccept(System.out::println);
        
        CompletableFuture
                .supplyAsync(() -> 88)
                .thenApply(x -> x + 1)
                .thenAccept(System.out::println);
    }
}
