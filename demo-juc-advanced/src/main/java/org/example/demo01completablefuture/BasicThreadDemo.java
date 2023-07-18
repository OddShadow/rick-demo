package org.example.demo01completablefuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class BasicThreadDemo {
    private static void doSomething() {}
    private static int doSomethingAndReturn() {return 0;}
    public static void main(String[] args) {
        // 第一种方式，Thread 构造注入 Runnable 接口
        new Thread(BasicThreadDemo::doSomething, "A").start();
    
        // 第二种方式，Thread 构造注入 FutureTask 类
        // FutureTask 类实现了 Runnable 接口，且通过 FutureTask 构造注入了一个 带指定返回值的 Runnable
        FutureTask<Integer> bFutureTask = new FutureTask<>(BasicThreadDemo::doSomething, 99);
        new Thread(bFutureTask, "B").start();
        try {
            System.out.println(bFutureTask.get(100, TimeUnit.MILLISECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    
        // 第三种方式，Thread 构造注入 FutureTask 类
        // FutureTask 类实现了 Runnable 接口，且通过 FutureTask 构造注入了一个 自带返回值的 Callable
        FutureTask<Integer> cFutureTask = new FutureTask<>(BasicThreadDemo::doSomethingAndReturn);
        new Thread(cFutureTask, "C").start();
        try {
            System.out.println(cFutureTask.get(100, TimeUnit.MILLISECONDS));
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
        
        // 除了手动 new Thread()，一般可以配合线程池使用
        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        FutureTask<Integer> dFutureTask = new FutureTask<>(BasicThreadDemo::doSomething, 99);
        FutureTask<Integer> eFutureTask = new FutureTask<>(BasicThreadDemo::doSomethingAndReturn);
        threadPool.submit(dFutureTask);
        threadPool.submit(eFutureTask);
        threadPool.shutdown();
    }
}
