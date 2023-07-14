package org.example.cdemo;

import org.example.utils.CpuUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CompletableFutureApi01 {
    public static void main(String[] args) {
        demo01();
        demo02();
        demo03();
        demo04();
        demo05();
    }
    private static void demo01() {
        CompletableFuture<Long> completableFuture = CompletableFuture.supplyAsync(() -> CpuUtils.doTask(1000L));
        Long result = null;
        try {
            // 1. 不离不弃
            result = completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        System.out.println("1. get() 获取结果" + "::" + result);
    }
    private static void demo02() {
        CompletableFuture<Long> completableFuture = CompletableFuture.supplyAsync(() -> CpuUtils.doTask(800L));
        Long result = null;
        try {
            // 2. 到时不候 超时 抛出 TimeoutException
            result = completableFuture.get(5000L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        System.out.println("2. get(5000L, TimeUnit.MILLISECONDS) 获取结果" + "::" + result);
    }
    private static void demo03() {
        CompletableFuture<Long> completableFuture = CompletableFuture.supplyAsync(() -> {
            // 3. 不离不弃 简化异常 有问题抛出 CompletionException
//            int i = 10 / 0; // 主动抛出异常
            return CpuUtils.doTask(600L);
        });
        Long result = completableFuture.join();
        System.out.println("3. join() 获取结果" + "::" + result);
    }
    private static void demo04() {
        CompletableFuture<Long> completableFuture = CompletableFuture.supplyAsync(() -> CpuUtils.doTask(1000L));
        // 4. 急不可待 默认值兜底
        Long result = completableFuture.getNow(88L);
        System.out.println("4. getNow() 未成功获取结果" + "::" + result);
        Long newResult = completableFuture.join();
        System.out.println("4. join() 成功获取结果" + "::" + newResult);
    }
    private static void demo05() {
        CompletableFuture<Long> completableFuture = CompletableFuture.supplyAsync(() -> CpuUtils.doTask(1000L));
        // 5. 急不可待 打断施法
        boolean defaultValue = completableFuture.complete(999L);
        Long result = completableFuture.join();
        System.out.println("5. complete() 配合 join() 获取结果" + "::" + result);
    }
}
