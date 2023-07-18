package org.example.demo01completablefuture.api;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.example.utils.CpuUtils.doTask;

/*
    有问题返回另一个
        1. applyToEither()
 */
public class CompletableFutureApi04 {
    private static final ExecutorService executors = Executors.newFixedThreadPool(6);
    public static void main(String[] args) {
        CompletableFuture<Long> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            long l = doTask(100L);
            System.out.printf("%-30s1111得到结果%d%n", Thread.currentThread().getName(), l);
            return l;
        }, executors);
        CompletableFuture<Long> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            long l = doTask(1000L);
            System.out.printf("%-30s2222得到结果%d%n", Thread.currentThread().getName(), l);
            return l;
        }, executors);
        CompletableFuture<String> completableFuture = completableFuture1.applyToEither(completableFuture2, f -> Thread.currentThread().getName() + "::竞争结果::[" + f + "]获胜");
        System.out.println(completableFuture.join());
        executors.shutdown();
    }
}
