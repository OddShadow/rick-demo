package org.example.cdemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CompletableFutureApi02 {
    
    private static final ExecutorService executors = Executors.newFixedThreadPool(6);
    
    public static void main(String[] args) throws InterruptedException {
        demo01();
        TimeUnit.SECONDS.sleep(3L);
        demo02();
    
        executors.shutdown();
    }
    // 1. thenApply() 线程的计算结果存在依赖关系，线程串行化执行
    private static void demo01() {
        CompletableFuture<Integer> completableFuture =
                CompletableFuture
                        .supplyAsync(() -> 10, executors)
                        .thenApply(f -> f + 20)
//                        .thenApply(f -> 10/0)
                        .thenApply(f -> f + 40)
                        .whenComplete((r, e) -> {
                            if (e == null) {
                                System.out.println(r);
                            }
                        })
                        .exceptionally(exception -> {
                            exception.printStackTrace();
                            return null;
                        });
    
        System.out.println(Thread.currentThread().getName() + "::doSomethingElse");
    }
    // 2. handle() 类似 try catch finally 至少可以继续下一个
    private static void demo02() {
        CompletableFuture<Integer> completableFuture =
                CompletableFuture
                        .supplyAsync(() -> 10, executors)
                        .handle((f, e) -> {
                            System.out.println("step1 - f===>" + f);
                            System.out.println("step1 - e===>" + e);
                            return f + 20;
                        })
                        .thenApply(f -> {
                            System.out.println("step2-1 - f===>" + f);
                            int i = 10/0;
                            System.out.println("step2-2 - f===>" + f);
                            return f;
                        })
                        .handle((f, e) -> {
                            System.out.println("step3 - f===>" + f);
                            System.out.println("step3 - e===>" + e);
                            return f + 40;
                        })
                        .whenComplete((r, e) -> {
                            if (e == null) {
                                System.out.println(r);
                            }
                        })
                        .exceptionally(exception -> {
                            exception.printStackTrace();
                            return null;
                        });
        System.out.println(Thread.currentThread().getName() + "::doSomethingElse");
    }
}
