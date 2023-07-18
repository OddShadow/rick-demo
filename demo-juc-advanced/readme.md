# Java 并发进阶

## 0. 简介

并发编程目的是为了提高程序性能，提高程序吞吐量，但是同时也带来了线程安全隐患
【并发】指宏观上同时，微观上有上下文切换
【并行】指真正的同时进行，多核CPU提供了实际应用
【进程】传统的操作系统中，进程既是基本的资源分配单元，也是基本的执行单元
【线程】线程是独立运行和独立调度的基本单位，Java 中的线程都是调用 `start0()` 使用 OS 的线程
【管程】在 Java 中一般指 Monitor
【用户线程】系统的工作线程
【守护线程】用户线程全部退出，守护线程自动退出，`isDaemon()` 方法可以获取
【临界区-Critical section】指不能被多个线程同时访问的区域，涉及对共享资源的访问，具体指被锁保护起来的代码块

## 1. CompletableFuture

### 1.1 `Runnable` `Callable` `FutureTask` `CompletableFuture` 类图

![](https://raw.githubusercontent.com/OddShadow/images/main/rick-demo/202307181124271.png)

### 1.2 回顾 `Runnable` `Callable` `FutureTask`

```java
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
```

使用 `FutureTask` 的遇到的问题

`FutureTask` 的 `get()` 方法阻塞当前线程，等待任务完成之后才可以获取返回值
`get(long timeout, TimeUnit unit)` 方法提供了超时处理措施
或者 `isDone()` 方法消耗 CPU 资源轮询等待

### 1.3 `CompletableFuture`

四种创建方法

1. `public static CompletableFuture<Void> runAsync(Runnable runnable)`
2. `public static CompletableFuture<Void> runAsync(Runnable runnable, Executor executor)`
3. `public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier)`
4. `public static <U> CompletableFuture<U> supplyAsync(Supplier<U> supplier, Executor executor)`

上面的创建方法，如果使用默认线程池
在 `java.util.concurrent.ForkJoinPool.registerWorker()` 中有 `wt.setDaemon(true)` ，表示线程被置为守护线程
同时CPU核心数大于 2 时才会使用 `ForkJoinPool` 否则直接创建线程不使用线程池

```java
public class CompletableFutureDemo02 {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        CompletableFuture
                .supplyAsync(() -> {
                    long l = doTask(1000L);
                    // int exceptionOperation = 10 / 0;
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
```

### 1.4 常用 API

1. 获得结果和触发计算

   1. `completableFuture.get()` 调用线程阻塞直到拿到返回值

   2. `completableFuture.get(2L, TimeUnit.SECONDS)` 调用线程阻塞直到拿到返回值，如果超时抛出 `TimeoutException`

   3. `completableFuture.join()` 调用线程阻塞直到拿到返回值，抛出非检查异常

   4. `completableFuture.getNow("defaultValue")` 立刻拿到返回值，如果任务没有完成则返回传参值

   5. `completableFuture.complete("defaultValue")` 立刻拿到返回值，如果任务没有完成则中断任务，将参数作为最终结果
2. 对计算结果进行处理

   1. `thenApply(f -> f)` 线程的计算结果存在依赖关系，线程串行化执行，上一步抛出异常，当前步骤不执行并且终止线程操作

   2. `handle((f, e) -> f)`线程的计算结果存在依赖关系，线程串行化执行，并且可以即使上一步抛出异常，依然可以继续执行当前步骤
3. 对计算结果进行消费
   1. `thenRun()` 使用 `Runnable` 消费，返回 `CompletableFuture<Void>`
   2. `thenAccept()` 使用 `Consumer` 消费，返回 `CompletableFuture<Void>`
4. 对计算速度进行选用
   1. `CompletableFuture applyToEither(CompletionStage other, Function fn)`
5. 对计算结果进行合并
   1. `CompletableFuture thenCombine(CompletionStage other, BiFunction fn)`

使用 `async` 尾缀的方法，会重新调用默认的 `ForkJoinPool` 线程池

## 2. Java 锁概述

锁的概念
计算机领域，锁-Lock 或者 互斥体-Mutex 被称为 同步原语-synchronization primitive，是一种限制多个线程访问共享资源的机制

1. 乐观锁 和 悲观锁
2. 公平锁 和 非公平锁
3. 可重入锁（递归锁）
4. 写锁（独占锁）和 读锁（共享锁）
5. 自旋锁 (SpinLock)
6. 无锁 -> 偏向锁 -> 轻量锁 -> 重量锁
7. 无锁 -> 独占锁 -> 读写锁 -> 邮戳锁
8. 死锁

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

## 3. 线程中断机制

```java
void interrupt() // 仅设置线程中断状态=true
static void interrupted() // 判断线程是否被中断 并 清除当前中断状态即中断状态=false
boolean isInterrupted() // 判断线程是否被中断，即返回中断状态值
```

### 3.1 线程中断机制

一个线程不应该由其它线程【强制中断】或【停止】，而是应该由线程本身自行停止
所以 `thread.stop()` `thread.suspend()` `thread.resume()` 已过时
Java 采取了【中断标识协商机制】
中断只是以一种协商机制，Java 没有增加任何中断语法，中断过程由程序员自己实现

### 3.2 停止运行中的线程的方法

除去已经过时的线程强制中断其它线程的方法【本质都是协商机制，只是协商的标志位不同】

1. `volatile` 变量实现

   ```java
   public class Method01 {
       private static volatile boolean isStop = false;
       public static void main(String[] args) {
           new Thread(() -> {
               while (true) {
                   if (isStop) {
                       System.out.println("end===");
                       break;
                   }
                   System.out.println("ing---");
               }
           }, "A").start();
           doTask(1000L);
           isStop = true;
       }
   }
   ```

2. `AtomicBoolean` 实现

   ```java
   public class Method02 {
       private static final AtomicBoolean atomicBoolean = new AtomicBoolean(false);
       public static void main(String[] args) {
           new Thread(() -> {
               while (true) {
                   if (atomicBoolean.get()) {
                       System.out.println("end===");
                       break;
                   }
                   System.out.println("ing---");
               }
           }, "A").start();
           doTask(1000L);
           atomicBoolean.set(true);
       }
   }
   ```

3. `Thread` 类自带的中断标识位

   ```java
   public class Method03 {
       public static void main(String[] args) {
           Thread aThread = new Thread(() -> {
               while (true) {
                   if (Thread.currentThread().isInterrupted()) {
                       System.out.println("end===");
                       break;
                   }
                   System.out.println("ing---");
               }
           }, "A");
           aThread.start();
           doTask(1000L);
           aThread.interrupt();
       }
   }
   ```

### 3.3 线程中断标识协商机制注意点

1. 仅仅设置指定线程 `flag=true` 不会影响指定线程的任何状态

2. 中断不活动的线程不会产生任何影响，返回 `false`

3. 如果线程处于阻塞状态如 `sleep` `wait` `join` 等状态，在别的线程中调用该线程的 `interrupt()` 方法
   阻塞线程立刻退出阻塞状态
   中断状态被清除
   抛出 `InterruptedException` 异常

   ```java
   public class Point03 {
       public static void main(String[] args) {
           Thread aThread = new Thread(() -> {
               while (true) {
                   try {
                       TimeUnit.SECONDS.sleep(1);
                   } catch (InterruptedException e) {
                       Thread.currentThread().interrupt(); // 这里不调用则会进入死循环
                       e.printStackTrace();
                   }
                   System.out.println("go");
               }
           }, "A-Thread");
           aThread.start();
   
           doTask(1000);
           // java.lang.InterruptedException: sleep interrupted
           aThread.interrupt();
       }
   }
   ```

4. 静态方法 `static void interrupted()`  判断线程是否被中断 并 清除当前中断状态即 中断状态 = false

## 4. LockSupport

`java.util.concurrent.locks.LockSupport`
Basic thread blocking primitives for creating locks and other synchronization classes

线程等待唤醒机制

`park()`用来阻塞线程 `unpark()` 用来解除阻塞线程
对应 `Object`的 `wait()` 和 `notify()` `JUC` 的 `await()` 和 `signal()`

`Object` 和 `JUC` 缺点

1. 【休眠方法和唤醒方法必须同步】否则抛出 `java.lang.IllegalMonitorStateException`
2. 【休眠方法必须先于唤醒方法之前调用】否则线程无限休眠

`LockSupport` 新方法

```java
static void park() // 线程无 permit 可用，阻塞当前线程，线程有 permit 可用，消耗然后退出
static void unpark(Thread thread) // 为给定线程发放 permit
```

`LockSupport`使用了一种 `Permit` 的概念完成阻塞线程和唤醒线程功能，每个线程都有一个 `permit`，但是于 `Semaphore`不同的地方是，`permit`累加上限为 1
不需要同步代码块，也无需考虑 休眠方法 和 唤醒方法 先后顺序

## 5. Java 内存模型 - JMM - Java Memory Model

### 1. 计算机硬件存储体系回顾

硬盘 - 内存 - CPU三级缓存 - CPU二级缓存 - CPU一级缓存 - 寄存器
由于各级读写速度不一致，JVM 规范试图定义一种内存模型 JMM 来屏蔽各种硬件和操作系统的内存访问差异
实现 Java 程序在各个平台下都能达到一致的内存访问效果

### 2. JMM 模型

JMM 本身是一种抽象的概念，仅仅是描述一组规范或者约定，通过这组描述或者约定定义了程序-尤其是多线程-各个变量的读写访问方式并决定一个线程对共享变量的何时写入以及如何变成对另一个线程可见
JMM 的关键技术点主要讨论 多线程的 【原子性】【可见性】【有序性】

### 3. JMM 三大特性

【原子性】
一个操作是不可打断的，多线程环境下，不会被其它线程影响

【可见性】
当一个线程修改了某一个共享变量的值，其它线程能够立刻知道变更，JMM 规定了所有的变量都存储在【主内存中】

- 主内存【内存】【存储共享变量 A】 
  - 线程 1 工作内存【CPU缓存或者CPU自己的寄存器】【存储共享变量 A 副本1】
  - 线程 2 工作内存【CPU缓存或者CPU自己的寄存器】【存储共享变量 A 副本2】

线程 1 本地工作内存修改了变量 A 之后会提交回主内存，但是这个过程线程 2 不一定感知到，所以导致了共享变量 A 对线程 2 不可见，即脏读

【有序性】
【源代码】- 【编译器优化重排】-【指令并行的重排】-【内存系统的重排】-【最终执行的指令】
为了提升性能，编译器和处理器可能会对指令序列进行重新排序，Java 规范规定 JVM 线程内维持顺序化语义，即只要程序的最终结果和它顺序化执行的结果相等，那么指令的执行顺序可以与代码顺序不一致，这个过程被程序指令的重排序

重点在于【指令重排可以保证串行语义一致，但是不保证多线程间语义也一致】
所以处理器在进行重排序时必须考虑指令间的数据依赖性

### 4. JMM 规范下，多线程对变量的读写过程

由于 JVM 运行程序的实体是线程，而每个线程创建时 JVM 都会为其创建一个工作内存(有些地方称为栈空间)，工作内存是每个线程的私有数据区域，而 Java 内存模型中规定所有变量都存储在主内存中，主内存是共享内存区域，所有线程都可以访问，但线程对变量的操作(读取赋值等)必须在工作内存中进行，首先要将变量从主内存拷贝到的线程自己的工作内存空间，然后对变量进行操作，操作完成后再将变量写回主内存，不能直接操作主内存中的变量，各个线程中的工作内存中存储着主内存中的变是副本拷贝，因此不同的线程间无法访问对方的工作内存，线程间的通信(传值)必须通过主内存来完成。

### 5. JMM 规范下，多线程先行发生原则之 happens-before

Java 内存模型是通过各种操作来定义的，包括对变量的读/写操作，监视器的加锁和释放操作，以及线程的启动和合并操作。JMM 为程序中所有的操作定义了一个【**偏序关系**】，称之为Happens-Before。要想保证执行操作B的线程看到操作A的结果（无论A和B是否在同一个线程中执行），那么在A和B之间必须满足 Happens-Before 关系。【**如果两个操作之间缺乏Happens-Before 关系，那么JVM 可以对它们任意地重排序。**】
当一个变量被多个线程读取并且至少被一个线程写人时，如果在读操作和写操作之间没有依照 Happens-Before 来排序，那么就会产生数据竞争问题。在正确同步的程序中不存在数据竞争，并会表现出串行一致性，这意味着程序中的所有操作都会按照一种固定的和全局的顺序执行。
虽然这些操作只满足偏序关系，但同步操作，如锁的获取与释放，volatile 变量的读取与写入，都满足【**全序关系**】，所以，在描述 Happens-Before 关系时，可以使用 "后续锁的获取操作" "后续 volatile 变量读取操作" 等表达术语

> 这里偏序关系 指并非所有的 操作A 和 操作B 都满足 Happens-Before
> 这里全序关系 指任意的 操作A 和 操作B 都满足 Happens-Before
> 锁的获取与释放，volatile 关键字，都是为了保证程序满足 Happens-Before 原则，即 满足以下八条规则，这样一定可以保证 操作A的结果 对于 操作B可见

```java
private int value = 0;
public int getValue() {
    return value;
}
public int setValue() {
    return ++value;
}
```

对于上面一段代码，如果线程A调用 get 方法，线程B调用 set 方法

八条规则

1. 【程序顺序规则】如果程序中操作A在操作B之前，那么在线程中A操作将在B操作之前执行
2. 【监视器锁规则】在监视器锁上的解锁操作必须在同一个监视器锁上的加锁操作之前执行
3. 【volatile 变量规则】对 volatile 变量的写入操作必须在对该变量的读操作之前执行
4. 【线程启动规则】线程上对 Thread.start 的调用必须在对该线程中执行任何操作之前执行
5. 【线程结束规则】线程中的任何操作都必须在其它线程检测到该线程已经结束之前执行，或者从 Thread.join 中成功返回，或者在调用 Thread.isAlive 时返回 false
6. 【中断规则】当一个线程在另一个线程上调用 interrupt 时，必须在被中断线程检测到 interrupt 调用之前执行（通过抛出 InterruptedException，或者调用 isInterrupted 和 interrupted）
7. 【终结器规则】对象的构造函数必须在启动该对象的终结器之前执行完成
8. 【传递性】如果操作 A 在操作 B 之前执行，并且操作 B 在操作 C 之前执行，那么操作 A 必须在操作 C 之前执行

## 6. volatile

JMM 模型定义了 8 种线程工作内存与主物理内存之间的原子操作
Read 读取 - Load 加载 - Use 用 - Assign 赋值 - Store 存储 - Write 写入 - Lock - UnLock

![](https://raw.githubusercontent.com/OddShadow/images/main/rick-demo/202307182003209.png)

**read**: 作用于主内存，将变量的值从主内存传输到工作内存，主内存到工作内存
**load**: 作用于工作内存，将 read 从主内存传输的变量值放入工作内存变量副本中，即数据加载
**use**: 作用于工作内存，将工作内存变量副本的值传递给执行引警，每当 JVM 遇到需要该变量的字节码指令时会执行该操作
**assign**: 作用于工作内存，将从执行引接收到的值赋值给工作内存变量，每当 JVM 遇到一个给变量赋值字节码指令时会执行该操作
**store**: 作用于工作内存，将赋值完毕的工作变量的值写回给主内存
**write**: 作用于主内存，将 store 传输过来的变量值赋值给主内存中的变量
由于上述 6 条只能保证单条指令的原子性，针对多条指令的组合性原子保证，没有大面积加锁，所以，JVM 提供了另外两个原子指令
**lock**: 作用于主内存，将一个变量标记为一个线程独占的状态，只是写时候加锁，就只是锁了写变量的过程。
**unlock**: 作用于主内存，把一个处于锁定状态的变量释放，然后才能被其他线程占用

### 内存屏障

CPU 或者 编译器对内存随机访问操作中的一个同步点，此点之前的所有读写操作都执行之后才可以开始执行此点之后的操作，其实就是一种 JVM 指令，JMM 的重排规则会要求 Java 编译器在生产 JVM 指令时插入特定的内存屏障指令，以此保证可见性和有序性
内存屏障之前的所有写操作都要回写主内存中
内存屏障之后的所有读操作都能获得内存屏障之前的所有写操作的最新结果，即可见性
【写屏障】【Store Memory Barrier】处理器在写屏障之前将所有存储在缓存(store bufferes)中的数据同步到主内存。也就是说当看到 Store屏障指令，就必须把该指令之前所有写入指令执行完毕才能继续往下执行。
【读屏障】【Load Memory Barrier】处理器在读屏障之后的读操作，都在读屏障之后执行。也就是说在 Load 屏障指令之后就能够保证后面的读取数据指令一定能够读取到最新的数据。

四种屏障类型

1. LoadLoad - `Load1;LoadLoad;Load2` - 保证 load1 后才可以 load2
2. StoreStore - `Store1;StoreStore;Store2` - 保证 store1 后才可以 store2
3. LoadStore - `Load1;LoadStore;Store2` - 保证 load1 后才可以 store2
4. StroeLoad - `Store1;StoreLoad;Load2` - 保证 store1 后才可以 load2

### volatile 特性

#### 可见性

场景：线程 A，线程 B，共享变量 M

问题1：线程 A 在本地内存中修改了 共享变量 M 的副本，却并没有把 共享变量 M 及时刷新回 主内存，导致其它线程不可知

问题2：线程 A 在本地内存中修改了 共享变量 M 的副本，并把 共享变量 M 及时刷新回 主内存, 但是线程 B 依然使用自己本地内存中旧的 共享变量 M 的副本，导致不可知

当对共享变量 M 使用 volatile 关键字后

- 线程写一个 volatile 变量，JMM 把该线程对应的本地内存中的共享变量值立刻刷新回主内存中
- 线程读一个 volatile 变量，JMM 把该线程对应的本地内存设置为无效，重新回主内存中读取最新共享变量

```java
public class Visibility {
    private static volatile boolean ready;
    private static volatile int number;
    public static void main(String[] args) {
        Thread aThread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "::start");
            while (!ready) {
            }
            System.out.println(Thread.currentThread().getName() + "::" + number);
        }, "A-thread");
        
        aThread.start();
        
        doTask(100L);
    
        number = 42;
        ready = true;
        System.out.println(Thread.currentThread().getName() + "::" + number);
        System.out.println(Thread.currentThread().getName() + "::" + ready);
    }
}
```

依此，解决线程间可见性

#### 无原子性

对加了 volatile 关键字的 number++ 操作多线程不安全

对于底层 `read` `load` `use` `assign` `store` `write` 一系列操作
`number++` 操作对于工作线程其实只保证了 `load` 主内存中最新的值，`store` 立刻刷新主内存中，而整个过程并不加锁，所以多个线程同时 `load` 先后 `store` 则会出现写覆盖，所以最终结果接近但是不等于目标值

从字节码也可以看出这一旦 `getstatic` 操作被执行后线程被抢占，则会出现写丢失

```java
 9 getstatic
12 iconst_1
13 iadd
14 putstatic
```

#### 指令禁重排

`https://tech.meituan.com/2014/09/23/java-memory-reordering.html`

一般情况下
如果不存在数据依赖时，可以重排序
如果存在数据依赖时，重排序可能会导致多线程下执行结果改变，比如【写后读 a=1;b=a;】【写后写 a=1;a=2;】【读后写 a=b;b=1】

使用 `volatile` 关键字，字节码会标识 `field` 为 `volatile` 类型，汇编码会加入内存屏障指令，禁止重排序

volatile 变量禁止重排序规则

| 第一个操作  | 第二个操作：普通读写 | 第二个操作：volatile 读 | 第二个操作：volatile 写 |
| ----------- | -------------------- | ----------------------- | ----------------------- |
| 普通读写    | 可以                 | 可以                    | 不可以                  |
| volatile 读 | 不可以               | 不可以                  | 不可以                  |
| volatile 写 | 可以                 | 不可以                  | 不可以                  |

`volatile `读之后插入一个 `LoadLoad` 屏障和一个 `LoadStore` 屏障，`volatile 读;LoadLoad;LoadStore;`
`volatile `写之前插入一个 `StoreStore` 屏障，之后插入一个 `StoreLoad` 屏障, `StoreStore;volatile 写;StoreLoad;`

## 7. CAS

### Java 实现

`java.util.concurrent.atomic`
CAS - `compare and swap` 主要包含 最新内存位置 V、预期内存位置原值 A、更新值 B
CAS 是一个原子操作【`cmpxchg 指令`】
本质是一种乐观锁
具体操作内容为，当且仅当旧的预期值 A 和内存位置 V 相同时，将内存位置 V 中的值修改为 B，否则什么都不做或者重来，其中重试的这一行为被称为【自旋】
多线程下性能优于 `synchronized`

### Unsafe 类

CAS 的核心类，由于 Java 方法无法访问底层系统，需要通过 `native` 方法访问，其中 Unsate 类相当于一个后门，基于该类可以直接操作特定内存的数据。Unsafe 类存在于 `sun.misc` 包中，其内部的方法全都是 `native` 修饰，可以像 C 指针一样操作内存，Java 中的 CAS 操作执行就依赖于 Unsafe 类的方法

### 代码实现原理

1. `new AtomicInteger(88);` 得到一个初始值为 88 的 AtomicInteger 类
2. AtomicInteger 类中 `private volatile int value;` 被构造函数初始为 88，用 `volatile` 关键字保证可见性，有序性
3. AtomicInteger 类中 `private static final long valueOffset;` 在静态代码块中调用 Unsafe 类得到内存偏移地址

```java
public final int getAndIncrement() {
    return unsafe.getAndAddInt(this, valueOffset, 1);
}
public final int getAndAddInt(Object var1, long var2, int var4) {
    int var5;
    do {
        var5 = this.getIntVolatile(var1, var2);
    } while(!this.compareAndSwapInt(var1, var2, var5, var5 + var4));

    return var5;
}
public native int getIntVolatile(Object var1, long var2);
public final native boolean compareAndSwapInt(Object var1, long var2, int var4, int var5);

/**
重点就是 getAndAddInt() 方法
当线程A getIntVolatile() 获取当前值为 88，线程B 抢占，先行修改 value=89 用 compareAndSwapInt() 修改失败后，再次获取 89，继续修改
所以本质是一种乐观锁
适合读多写少场景，并且可以保证代码线程安全
*/
```

c++

```C++
UNSAFE_ENTRY(jboolean, Unsafe_CompareAndSwapInt(JNIEnv *env, jobject unsafe, jobject obj, jlong offset, jint e, jint x))
UnsafeWrapper("Unsafe_CompareAndSwapInt");
oop p = JNIHandles::resolve(obj);
jint* addr = (jint *)index_oop_from_field_offset_long(p, offset);
return (jint) (Atomic::cmpxchg(x, addr, e)) == e;
UNSAFE_END
    
unsigned Atomic::cmpxchg(unsigned int exchange_value, volatile int* dest, unsigned int compare_value) {
    assert(sizeof(unsigned int) == sizeof(jint), "...")
    return (unsigned int)Atomic::cmpxchg((jint)exchange_value, (volatitle jint*)dest, (jint) compare_value;
}

// windows10 下调用的方法
inline jint Atomic::cmpxchg(jint exchange_value, volatile jint* dest, jint compare_value) {
    int mp = os::is_MP();
    _asm {
        mov edx, dest
        mov ecx, exchange_value
        mov eax, compare_value
        LOCK_IF_MP(mp)
        cmpxchg dword ptr [edx], ecx
    }
}
```

先拿到对象绝对地址，根据偏移量 valueOffset 计算 value 的绝对地址
再调用 Atomic 的 cmpxchg 函数执行 交换并比较操作
修改成功 返回 true 修改失败 返回 false
其中 JDK 提供的 CAS 机制，再汇编层会禁止变量两侧的指令优化，并且 cmpxchg 是一个原子操作

### spinlock - 自旋锁

CAS 是实现自旋锁的基础，利用 CPU 指令保证了操作的原子性，主要是通过【循环的方式】尝试获取锁，好处是减少线程切换带来的上下文切换损耗，缺点是循环导致 CPU 消耗

手写自旋锁 `org.example.demo_8.MySpinLock`

### CAS 缺点

1. 循环时间长导致开销过大
2. ABA 问题 【比较值虽然相等，但是可能 A 是被修改了 两次，先改成 B，再改回 A】
   解决方法可以加入 stamped ，Java 实现可以用 
   `AtomicStampedReference` 记录修改过几次
   `AtomicMarkableReference` 记录是否修改

## 9. 原子操作类

```java
AtomicBoolean
AtomicInteger
AtomicIntegerArray
AtomicIntegerFieldUpdater
AtomicLong
AtomicLongArray
AtomicLongFieldUpdater
AtomicMarkableReference
AtomicReference
AtomicReferenceArray
AtomicReferenceFieldUpdater
AtomicStampedReference
DoubleAccumulator
DoubleAdder
LongAccumulator
LongAdder
```

1. 基本类型原子类

   - `AtomicBoolean`
   - `AtomicInteger`
   - `AtomicLong`

   ```java
   public final int get();
   public final int getAndSet(int newValue);
   public final int getAndIncrement();
   public final int getAndDecrement();
   public final int getAndAdd(int delta);
   boolean compareAndSet(int expect, in update);
   ```

2. 数组类型原子类

   - `AtomicIntegerArray`
   - `AtomicLongArray`
   - `AtomicReferenceArray`

3. 引用类型原子类

   - `AtomicReference`
   - `AtomicStampedReference`
   - `AtomicMarkableReference`

4. 对象的属性修改原子类

   - `AtomicIntegerFieldUpdater`
   - `AtomicLongFieldUpdater`
   - `AtomicReferenceFieldUpdater`

   以线程安全的方式操作非线程安全对象中指定的 `volatile int` `volatile long` `volatile 引用`

   1. 更新的对象必须使用 `public volatile` 修饰
   2. 因为对象的属性修改类型原子类都是抽象类，所以每次使用都必须使用静态方法 `newUpdater()` 创建更新器，并设置要更新的类和属性

5. 原子操作增强类原理深度解析

## 10. ThreadLocal

## 11. Java 对象内存布局和对象头

问题探讨：`Object obj = new Object()` 占用多少内存空间

HotSpot 虚拟机中，对象在【堆内存】中的存储布局分为三个部分：

1. 【对象头 Header】
   - 【对象标记 Mark Word】【存储 哈希码，GC 标记，GC 次数，同步锁标记，偏向锁持有者】【占用8个字节】
   - 【类元信息（类型指针）Class Pointer 】组成【指向方法区的类元 Klass 信息】【不考虑压缩指针，占用8个字节】
2. 【实例数据 Instance Data】【存放类的 Field 数据信息，包括父类 Field 信息】
3. 【对齐填充 Padding】【保证对象存储为 8 个字节的倍数】

64 bit 虚拟机

|          |                             |                   |          |              |                    |                  |
| -------- | --------------------------- | ----------------- | -------- | ------------ | ------------------ | ---------------- |
| 锁状态   | 56 bit - 1                  | 56 bit - 2        |          |              |                    |                  |
|          | 25bit                       | 31 bit            | 1 bit    | 4 bit        | 1 bit - 是否偏向锁 | 2 bit - 锁标志位 |
| 无锁     | unused                      | hashcode          | cms_free | 对象分代年龄 | 0                  | 01               |
| 偏向锁   | threadId-54bit-偏向锁线程ID | epoch-2bit-时间戳 | cms_free | 对象分代年龄 | 1                  | 01               |
| 轻量级锁 | 指向栈中锁的记录的指针      | ==>               | ==>      | ==>          | ==\|               | 00               |
| 重量级锁 | 指向重量级锁的指针          | ==>               | ==>      | ==>          | ==\|               | 10               |
| GC 标志  | 空                          | ==>               | ==>      | ==>          | ==\|               | 11               |

工具 - JOL - 分析对象在 JVM 中的大小和分布

```xml
<dependency>
    <groupId>org.openjdk.jol</groupId>
    <artifactId>jol-core</artifactId>
    <version>0.14</version>
</dependency>
```

对 `Object obj  = new Object()`

```java
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header)                           e5 01 00 f8 (11100101 00000001 00000000 11111000) (-134217243)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
```

对 `CustomizedObject customizedObject = new CustomizedObject()`

```java
org.example.demo_11.CustomizedObject object internals:
 OFFSET  SIZE       TYPE DESCRIPTION               VALUE
      0     4            (object header)           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4            (object header)           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4            (object header)           db 23 01 f8 (11011011 00100011 00000001 11111000) (-134143013)
     12     4        int  CustomizedObject.age     0
     16     4   java.lang.String CustomizedObject.name    null
     20     4            (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
```

压缩指针

指对象头的类型指针本应该是8个字节
使用 JVM 指令 `-XX:+PrintCommandLineFlags` 可以打印 JVM 默认参数
`-XX:InitialHeapSize=536064960 -XX:MaxHeapSize=8577039360 -XX:+PrintCommandLineFlags -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseLargePagesIndividualAllocation -XX:+UseParallelGC`
其中 `-XX:+UseCompressedClassPointers` 就是表示开启了压缩指针命令， 将类型指针的占用压缩成 4 字节
使用 JVM 指令 `-XX:-PrintCommandLineFlags` 可以关闭 压缩指针

## 12. Synchronized 与锁升级

在 Java5 之前，`Synchronized` 是操作系统级别的重量级锁，Java6 之后引入了轻量级锁和偏向锁

解释：Java 线程调用 `native void start0()` 方法，底层是调用操作系统原生线程，当需要阻塞或者唤醒一个线程时，就需要操作系统介入，CPU 从用户态切换为内核态，保存用户态的寄存器，存储空间等上下文，这个过程消耗大量系统资源，在系统内核态调用结束后，还原上下文再切换回用户态继续工作。如果同步代码块中的代码很简单，那么可能切换状态的时间比执行代码的时间更长。

每个对象都可以成为锁

解释：其实也就是每个对象都是一个 `ObjectMonitor`，源码中给每个对象都带了一个内部锁，而这个锁 `Monitor` 又被称为管程，本质是依赖于底层操作系统 `Mutex Lock` 实现，需要用户态和内核态切换，成本非常高

Monitor、Java 对象、线程

如果 Java 对象被一个线程锁住
就是 Java 对象的 Mark Word 中的 LockWord 指向了 Monitor 的起始地址，Monitor 的 Owner 字段会存放用户相关对象锁的线程 ID

### 12.1 无锁

```java
public class NoLock {
    public static void main(String[] args) {
        // 小端存储，64bit 高地址位置 存 高字节
        Object obj = new Object();
        System.out.println(Integer.toBinaryString(obj.hashCode())); // 调用才有
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
    }
}
```

```java
1110100 10100001 01000100 10000010
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 82 44 a1 (00000001 10000010 01000100 10100001) (-1589345791)
      4     4        (object header)                           74 00 00 00 (01110100 00000000 00000000 00000000) (116)
      8     4        (object header)                           e5 01 00 f8 (11100101 00000001 00000000 11111000) (-134217243)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
```

无锁状态，64bit 虚拟机，小端模式存储，对应如下

| 锁状态 | 25bit                        | 31bit                              | 1bit   | 4bit     | 1bit       | 2bit         |
| ------ | ---------------------------- | ---------------------------------- | ------ | -------- | ---------- | ------------ |
| 无锁   | unused                       | HashCode - 调用才有                | unused | 分代年龄 | 偏向锁 - 0 | 锁标志 - 0 1 |
|        | 0 00000000 00000000 00000000 | 1110100 10100001 01000100 10000010 | 0      | 0000     | 0          | 01           |

### 12.2 偏向锁

偏向锁适用在单线程竞争中
线程A第一次获得锁后，修改 Mark Word 中的偏向锁 ID，偏向模式
如果不存在其它线程竞争，那么持有偏向锁的线程不需要同步操作，避免了上下文切换

锁在第一次被拥有时，只需要在对象头中记录下偏向线程ID，这样代表偏向线程一直持有着锁，如果下次同一个线程进入同一个锁
如果线程ID 相等，即检查到对象头中存放的是自己的线程ID，则无需上下文切换，直接进入同步
如果线程ID 不相等，则表示发生了锁竞争，则尝试使用 CAS 把对象头线程ID 更新为自己的线程ID
竞争成功，表示之前的线程不存在，更新线程ID，锁无需升级，依然是偏向锁
竞争失败，则需要升级成轻量级锁才可以保证安全性，等待到全局安全点，撤销偏向锁
偏向锁只有需要其他线程竞争时，持有偏向锁的线程才会释放锁，线程本身不会主动释放锁

偏向锁的撤销
一、第一个线程正在执行 `synchronized` 方法，处于同步代码块中，其它线程竞争，偏向锁被撤销，并锁升级，此时轻量级锁由原持有偏向锁的线程持有，同步代码继续执行，而竞争的线程则会进入自旋等待获取改轻量级锁
二、第一个线程执行完成 `synchronized` 方法，退出同步代码块，则讲对象头设置成无锁状态，撤销偏向锁，重新偏向

`https://segmentfault.com/a/1190000041194920`

`-XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0`

```java
-XX:+PrintFlagsInitial | grep BiasedLock*
intx BiasedLockingStartupDelay  = 4000 // 线程启用偏向锁有4秒延迟
bool UseBiasedLocking           = true // 偏向锁默认打开, 如果关闭则直接升级为轻量级锁
```

```java
// -XX:BiasedLockingStartupDelay=0
public class BiasedLock {
    public static void main(String[] args) {
        Object obj = new Object();
        synchronized (obj) {
            System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        }
    }
}
```

```java
public class BiasedLock02 {
    public static void main(String[] args) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Object obj = new Object();
        synchronized (obj) {
            System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        }
    }
}
```

```java
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           05 98 3b 51 (00000101 10011000 00111011 01010001) (1362860037)
      4     4        (object header)                           af 01 00 00 (10101111 00000001 00000000 00000000) (431)
      8     4        (object header)                           e5 01 00 f8 (11100101 00000001 00000000 11111000) (-134217243)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
```

偏向锁状态，64bit 虚拟机，小端模式存储，对应如下

| 锁状态 | 54bit                                                        | 2bit  | 1bit   | 4bit     | 1bit       | 2bit         |
| ------ | ------------------------------------------------------------ | ----- | ------ | -------- | ---------- | ------------ |
| 偏向锁 | 当前线程指针 JavaThread* 就是线程ID                          | Epoch | unused | 分代年龄 | 偏向锁 - 1 | 锁标志 - 0 1 |
|        | 00000000 00000000 00000001 10101111 01010001 00111011 100110 | 00    | 0      | 0000     | 1          | 01           |

### 12.3 轻量级锁

轻量级锁场景
多线程竞争，但是任意时刻最多只有一个线程竞争，即不存在锁竞争太过激烈的情况，没有线程阻塞

轻量级锁状态，64bit 虚拟机，小端模式存储，对应如下

| 锁状态 | 62bit                           | 2bit         |
| ------ | ------------------------------- | ------------ |
| 偏向锁 | 指向线程栈中 Lock Record 的指针 | 锁标志 - 0 0 |
|        |                                 | 00           |

### 理解

高并发下，同步调用需要考虑锁带来的损耗问题，使用锁可以实现数据安全性，但是会导致性能下降，无锁能够提升程序性能，但是会导致并发安全问题，两者之间需要平衡。
由对象头中 Mark Word 根据锁标志位的不同而被复用，以及锁升级策略
锁的升级过程，无锁，偏向锁，轻量级锁，重量级锁

## 13. AQS 源码分析

![](https://raw.githubusercontent.com/OddShadow/images/main/rick-demo/202307172250145.png)

## 14. ReentrantLock` `ReentrantReadWriteLock` `StampedLock

ReentrantReadWriteLock
仅被多个读线程访问，或者仅被一个写线程访问

对于读多写少场景，读共享可以提高并发性能

注意点

1. 写饥饿
2. 锁降级