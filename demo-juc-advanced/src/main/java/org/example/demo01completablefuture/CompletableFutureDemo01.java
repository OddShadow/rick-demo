package org.example.demo01completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// 4种创建 CompletableFuture 的方式
// 创建之后都是直接操作，并且 FutureTask 可以做的，CompletableFuture 也可以做
public class CompletableFutureDemo01 {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture.runAsync(() -> System.out.printf("%-33s::执行了:: %s%n", Thread.currentThread().getName(), "CompletableFuture.runAsync(Runnable runnable)"));
        CompletableFuture.runAsync(() -> System.out.printf("%-33s::执行了:: %s%n", Thread.currentThread().getName(), "CompletableFuture.runAsync(Runnable runnable, Executor executor)"), threadPool);
        System.out.printf(CompletableFuture.supplyAsync(() -> String.format("%-33s::执行了:: %s%n", Thread.currentThread().getName(), "CompletableFuture.supplyAsync(Supplier<U> supplier)")).join());
        System.out.printf(CompletableFuture.supplyAsync(() -> String.format("%-33s::执行了:: %s%n", Thread.currentThread().getName(), "CompletableFuture.supplyAsync(Supplier<U> supplier, Executor executor)"), threadPool).join());
        threadPool.shutdown();
    }
}
