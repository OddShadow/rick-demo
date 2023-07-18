package org.example.demo01completablefuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

// FutureTask 基本api使用
public class FutureTaskDemo01 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> futureTask = new FutureTask<>(() -> Thread.currentThread().getName() + "::hello future-task");
        new Thread(futureTask, "A-Thread").start();
        System.out.println(futureTask.get());
    }
}