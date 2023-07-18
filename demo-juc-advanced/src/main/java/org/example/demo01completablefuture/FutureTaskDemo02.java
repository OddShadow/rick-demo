package org.example.demo01completablefuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static org.example.utils.CpuUtils.doTask;

/*
 优点::可以和线程池配合显著提高程序执行效率
 缺点::futureTask.get()方法有潜在的阻塞隐患
 缺点::futureTask.isDone()方法轮询占用cpu资源
 
 可以优化的功能:
    1. 回调通知
    2. 多个线程之间的依赖关系
*/
public class FutureTaskDemo02 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        noThreadTime();
        useThreadTime();
    }
    
    private static void noThreadTime() {
        long startTime = System.currentTimeMillis();
        long sum = 0L;
        sum += doTask(1000L);
        sum += doTask(500L);
        sum += doTask(200L);
        long endTime = System.currentTimeMillis();
        System.out.printf(">> 顺序执行costTime=%05dms 得到结果::%d%n", endTime-startTime, sum);
    }
    
    private static void useThreadTime() throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        long sum = 0L;
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        FutureTask<Long> futureTask1 = new FutureTask<>(() -> doTask(1000L));
        FutureTask<Long> futureTask2 = new FutureTask<>(() -> doTask(500L));
        FutureTask<Long> futureTask3 = new FutureTask<>(() -> doTask(200L));
        threadPool.submit(futureTask1);
        threadPool.submit(futureTask2);
        threadPool.submit(futureTask3);
        
        // 1. futureTask1.get()
        // 在这里其实 futureTask2 和 futureTask3 已经得到结果但是由于先执行的 futureTask1.get() 所以线程被阻塞了
        sum += futureTask1.get();
        
        // 2. futureTask1.get(200, TimeUnit.MILLISECONDS)
        // 表示只等 200ms 没有结果则抛出 TimeoutException, 算是被阻塞的一种处理方式，简单粗暴
//        try {
//            sum += futureTask1.get(200, TimeUnit.MILLISECONDS);
//        } catch (TimeoutException e) {
//            threadPool.shutdownNow();
//            throw new RuntimeException(e);
//        }
        
        // 3. futureTask1.isDone()
        // 轮询等待可以给出一些提示信息的操作
//        while (!futureTask1.isDone()) {
//            System.out.println("等待 futureTask1 出结果");
//        }
//        sum += futureTask1.get();

        sum += futureTask2.get();
        sum += futureTask3.get();
        threadPool.shutdownNow();
        long endTime = System.currentTimeMillis();
        System.out.printf(">> 并发执行costTime=%05dms 得到结果::%d%n", endTime-startTime, sum);
    }
    
}