package org.example.bdemo;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.example.utils.CpuUtils.doTask;

public class CompletableFutureDemo02 {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture
                .supplyAsync(() -> {
                    long l = doTask(1000L);
//                    int exceptionOperation = 10 / 0;
                    return l;
                }, threadPool)
                .whenComplete((result, exception) -> {
                    if (exception == null) {
                        System.out.printf("线程正常执行完毕，得到结果::%d", result);
                    }
                })
                .exceptionally(exception -> {
                    exception.printStackTrace();
                    System.out.println("线程执行异常");
                    return null;
                });
        System.out.println("主线程未被阻塞");
        threadPool.shutdown();
    }
}
