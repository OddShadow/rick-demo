# Java 并发进阶

## 1. CompletableFuture

### 常用 API

1. 获得结果和触发计算

   1. `completableFuture.get()`

      调用线程阻塞直到拿到返回值

   2. `completableFuture.get(2L, TimeUnit.SECONDS)`

      调用线程阻塞直到拿到返回值，如果超时抛出 `TimeoutException`

   3. `completableFuture.join()`

      调用线程阻塞直到拿到返回值，封装异常，不用手动处理

   4. `completableFuture.getNow("defaultValue")`

      立刻拿到返回值，如果任务没有完成则返回传参值

   5. `completableFuture.complete("defaultValue")`

      立刻拿到返回值，如果任务没有完成则中断任务，将参数作为最终结果

2. 对计算结果进行处理

   1. `thenApply(f -> f)` 

      线程的计算结果存在依赖关系，线程串行化执行，上一步抛出异常，当前步骤不执行并且终止线程操作

   2. `handle((f, e) -> f)`

      线程的计算结果存在依赖关系，线程串行化执行，并且可以即使上一步抛出异常，依然可以继续执行当前步骤

3. 对计算结构进行消费

4. 对计算速度进行选用

5. 对计算结果进行合并

## 2. Java 锁实现

### 乐观锁 和 悲观锁

- 悲观锁
  适合写多的场景，加锁保证资源在持有锁期间不会被其它线程修改 `synchronized` 和 `Lock` 都是悲观锁
- 乐观锁
  先查后更新，更新时检查数据是否在查出后被其它线程修改了，如果没有修改则更新成功，`version`机制，`CAS` 算法

### synchronized 的三种基本使用

1. 同步代码块 锁指定对象
2. 同步方法 锁 this 对象
3. 同步静态方法 锁 Class 类

> 翻阅 JDK 源码可知 ObjectMonitor.java -> ObjectMonitor.cpp -> objectMonitor.hpp
> 对于 Java 中每个对象都继承 Object 对象，所以都有 ObjectMonitor 对象，对象中有属性 _owner 指向持有该对象锁的线程

### 公平锁 和 非公平锁

- 公平锁
  多个线程按照申请锁的顺序来获取锁，由于频繁上下文切换线程开销会比非公平锁大
  `ReentrantLock lock = new ReentrantLock(true)`

- 非公平锁

  多个线程获取获得锁的顺序由系统分配，高并发环境下有可能导致 优先级翻转 或者 线程饥饿
  `ReentrantLock lock = new ReentrantLock()`

### 可重入锁

如果对已经上锁的普通互斥锁进行“加锁”操作，其结果要么失败，要么会阻塞至解锁。
而如果换作可重入互斥锁，当且仅当尝试加锁的线程就是持有该锁的线程时，类似的加锁操作就会成功。
可重入互斥锁一般都会记录被加锁的次数，只有执行相同次数的解锁操作才会真正解锁。
可重入解决了普通互斥锁不可重入的问题，如果函数先持有锁，然后执行回调，但回调的内容是调用它自己，就会产生死锁。
`synchronized` 和 `Lock` 都是可重入锁

> 翻阅 JDK 源码可知 ObjectMonitor 对象有一个 锁重入次数 _recursions 和 记录该线程获取锁的次数 _count 实现锁的重入

### 死锁

当两个以上的线程，双方都在等待对方停止执行，以取得系统资源，但是没有一方提前退出时，就称为死锁

死锁的四个原因

1. 禁止抢占 系统资源不能被强制从一个进程中退出
2. 持有和等待 一个进程可以在等待时持有系统资源
3. 互斥 资源只能同事分配给一个进程，无法多个进程公用
4. 循环等待 一系列进程互相持有其它进程所需要的资源

Java 排查死锁

1. 查看 java 进程`jps -l` 和 排查进程死锁 `jstack ***`
2. 视图 `jconsole`

## 3. LockSupport 和 线程中断