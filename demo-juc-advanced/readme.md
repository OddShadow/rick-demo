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

## 5. LockSupport 和 线程中断

```java
java.util.concurrent.locks.LockSupport
void interrupt() // 设置线程中断状态=true
static void interrupted() // 判断线程是否被中断 并 清除当前中断状态即中断状态=false
boolean isInterrupted() // 判断线程是否被中断，即返回中断状态值
```

### 5.1 线程中断机制

一个线程不应该由其它线程【强制中断】或【停止】，而是应该由线程本身自行停止，
所以 `thread.stop()` `thread.suspend()` `thread.resume()` 已过时
Java 采取了【中断标识协商机制】
中断只是以一种协商机制，Java 没有增加任何中断语法，中断过程由程序员自己实现

### 5.2 停止运行中的线程的方法

除去已经过时的线程强制中断其它线程的方法【本质都是协商机制，只是协商的标志位不同】

1. `volatile` 变量实现 - demo【`org.example.demo_5.a_stopthreadmethod.Method01`】
2. `AtomicBoolean` 实现 - demo【`org.example.demo_5.a_stopthreadmethod.Method02`】
3. `Thread` 类自带的中断 api 实例方法实现 - demo【`org.example.demo_5.a_stopthreadmethod.Method02`】

### 5.3 线程中断标识协商机制注意点

1. `void interrupt()` 【仅仅设置指定线程 `flag=true` 不会影响指定线程的任何状态】，需要自己编写代码判断处理 - dmeo【`org.example.demo_5.b_important.Point01`】
2. 中断不活动的线程不会产生任何影响，返回 `false` - demo【`org.example.demo_5.b_important.Point02`】
3. 如果线程处于阻塞状态，`sleep` `wait` `join` 在别的线程中调用阻塞线程的 `void interrupt()` 方法，阻塞线程立刻退出阻塞状态，【中断状态被清除】，并抛出 `InterruptedException` 异常 - demo【`org.example.demo_5.b_important.Point03`】
4. 静态方法 `static void interrupted()`  判断线程是否被中断 并 清除当前中断状态即 中断状态 = false - demo 【`org.example.demo_5.b_important.Point04`】

### 5.4 LockSupport 和 线程等待唤醒机制

用来创建锁和其他同步类的基本线程阻塞原语
`park()`用来阻塞线程 `unpark()` 用来接触阻塞线程
同样的，`Object`的 `wait()` 和 `notify()`
`JUC` 的 `await()` 和 `signal()`

#### `Object` 和 `JUC` 必要条件 

1. 【休眠方法和唤醒方法必须同步】否则抛出 `java.lang.IllegalMonitorStateException`
2. 【休眠方法必须先于唤醒方法之前调用】否则线程无限休眠

#### `LockSupport` 新方法

```java
static void park() // 除非 permit 可用，否则阻塞当前线程
static void unpark(Thread thread) // 如果给定线程阻塞，为其发放 permit
```

`LockSupport`使用了一种 `Permit` 的概念完成阻塞线程和唤醒线程功能，每个线程都有一个 `permit`，但是于 `Semaphore`不同的地方是，`permit`累加上限为 1
不需要同步代码块，也无需考虑 休眠方法 和 唤醒方法 先后顺序

## 6. Java 内存模型 - JMM - Java Memory Model

### 1. 面试题

- 什么是 Java 内存模型 JMM
- JMM 和 volatile 之间的关系
- JMM 有哪些特性，它的三大特性是什么
- 为什么需要 JMM，作用和功能是什么
- happens-before 先行发生原则你有了解过嘛

### 2. 计算机硬件存储体系回顾

硬盘 - 内存 - CPU三级缓存 - CPU二级缓存 - CPU一级缓存 - 寄存器
由于各级读写速度不一致，JVM 规范试图定义一种内存模型 JMM 来屏蔽各种硬件和操作系统的内存访问差异
实现 Java 程序在各个平台下都能达到一致的内存访问效果

### 3. JMM 模型

JMM 本身是一种抽象的概念，仅仅是描述一组规范或者约定，通过这组描述或者约定定义了程序-尤其是多线程-各个变量的读写访问方式并决定一个线程对共享变量的何时写入以及如何变成对另一个线程可见
JMM 的关键技术点主要讨论 多线程的 【原子性】【可见性】【有序性】

### 4. 原子性 可见性 有序性

#### 原子性

一个操作是不可打断的，多线程环境下，不会被其它线程影响

#### 可见性

当一个线程修改了某一个共享变量的值，其它线程能够立刻知道变更，JMM 规定了所有的变量都存储在【主内存中】

- 主内存【内存】【存储共享变量 A】 
  - 线程 1 工作内存【CPU缓存或者CPU自己的寄存器】【存储共享变量 A 副本1】
  - 线程 2 工作内存【CPU缓存或者CPU自己的寄存器】【存储共享变量 A 副本2】

线程 1 本地工作内存修改了变量 A 之后会提交回主内存，但是这个过程线程 2 不一定感知到，所以导致了共享变量 A 对线程 2 不可见，即脏读

#### 有序性

【源代码】- 【编译器优化重排】-【指令并行的重排】-【内存系统的重排】-【最终执行的指令】
为了提升性能，编译器和处理器可能会对指令序列进行重新排序，Java 规范规定 JVM 线程内维持顺序化语义，即只要程序的最终结果和它顺序化执行的结果相等，那么指令的执行顺序可以与代码顺序不一致，这个过程被程序指令的重排序

重点在于【指令重排可以保证串行语义一致，但是不保证多线程间语义也一致】
所以处理器在进行重排序时必须考虑指令间的数据依赖性

### 5. JMM 规范下，多线程对变量的读写过程

由于 JVM 运行程序的实体是线程，而每个线程创建时 JVM 都会为其创建一个工作内存(有些地方称为栈空间)，工作内存是每个线程的私有数据区域，而 Java 内存模型中规定所有变量都存储在主内存中，主内存是共享内存区域，所有线程都可以访问，但线程对变量的操作(读取赋值等)必须在工作内存中进行，首先要将变量从主内存拷贝到的线程自己的工作内存空间，然后对变量进行操作，操作完成后再将变量写回主内存，不能直接操作主内存中的变量，各个线程中的工作内存中存储着主内存中的变是副本拷贝，因此不同的线程间无法访问对方的工作内存，线程间的通信(传值)必须通过主内存来完成。

### 6. JMM 规范下，多线程先行发生原则之 happens-before

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

#### 八条规则

1. 【程序顺序规则】如果程序中操作A在操作B之前，那么在线程中A操作将在B操作之前执行
2. 【监视器锁规则】在监视器锁上的解锁操作必须在同一个监视器锁上的加锁操作之前执行
3. 【volatile 变量规则】对 volatile 变量的写入操作必须在对该变量的读操作之前执行
4. 【线程启动规则】线程上对 Thread.start 的调用必须在对该线程中执行任何操作之前执行
5. 【线程结束规则】线程中的任何操作都必须在其它线程检测到该线程已经结束之前执行，或者从 Thread.join 中成功返回，或者在调用 Thread.isAlive 时返回 false
6. 【中断规则】当一个线程在另一个线程上调用 interrupt 时，必须在被中断线程检测到 interrupt 调用之前执行（通过抛出 InterruptedException，或者调用 isInterrupted 和 interrupted）
7. 【终结器规则】对象的构造函数必须在启动该对象的终结器之前执行完成
8. 【传递性】如果操作 A 在操作 B 之前执行，并且操作 B 在操作 C 之前执行，那么操作 A 必须在操作 C 之前执行

## 7. volatile 和 JMM

### 1. 原子操作

JMM 模型定义了 8 种线程工作内存与主物理内存之间的原子操作
Read 读取 - Load 加载 - Use 用 - Assign 赋值 - Store 存储 - Write 写入 - Lock - UnLock

<img src="doc/image/image-001.png" alt="image-001" style="zoom:200%;" />

**read**: 作用于主内存，将变量的值从主内存传输到工作内存，主内存到工作内存
**load**: 作用于工作内存，将 read 从主内存传输的变量值放入工作内存变量副本中，即数据加载
**use**: 作用于工作内存，将工作内存变量副本的值传递给执行引警，每当 JVM 遇到需要该变量的字节码指令时会执行该操作
**assign**: 作用于工作内存，将从执行引接收到的值赋值给工作内存变量，每当 JVM 遇到一个给变量赋值字节码指令时会执行该操作
**store**: 作用于工作内存，将赋值完毕的工作变量的值写回给主内存
**write**: 作用于主内存，将 store 传输过来的变量值赋值给主内存中的变量
由于上述 6 条只能保证单条指令的原子性，针对多条指令的组合性原子保证，没有大面积加锁，所以，JVM 提供了另外两个原子指令
**lock**: 作用于主内存，将一个变量标记为一个线程独占的状态，只是写时候加锁，就只是锁了写变量的过程。
**unlock**: 作用于主内存，把一个处于锁定状态的变量释放，然后才能被其他线程占用

### 2.【内存屏障】

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

### 3. volatile 特性之可见性

`org.example.demo_7.Visibility`

场景：线程 A，线程 B，共享变量 M

问题1：线程 A 在本地内存中修改了 共享变量 M 的副本，却并没有把 共享变量 M 及时刷新回 主内存，导致其它线程不可知
问题2：线程 A 在本地内存中修改了 共享变量 M 的副本，并把 共享变量 M 及时刷新回 主内存, 但是线程 B 依然使用自己本地内存中旧的 共享变量 M 的副本，导致不可知

当对共享变量 M 使用 volatile 关键字后
- 线程写一个 volatile 变量，JMM 把该线程对应的本地内存中的共享变量值立刻刷新回主内存中
- 线程读一个 volatile 变量，JMM 把该线程对应的本地内存设置为无效，重新回主内存中读取最新共享变量

依此，解决线程间可见性

### 4. volatile 特性之原子性 - 没有

`org.example.demo_7.NoAtomic` 对加了 volatile 关键字的 number++ 操作多线程不安全

对于底层 `read` `load` `use` `assign` `store` `write` 一系列操作
`number++` 操作对于工作线程其实只保证了 `load` 主内存中最新的值，`store` 立刻刷新主内存中，而整个过程并不加锁，所以多个线程同时 `load` 先后 `store` 则会出现写覆盖，所以最终结果接近但是不等于目标值

从字节码也可以看出这一旦 getstatic 操作被执行后线程被抢占，则会出现写丢失

```java
 9 getstatic
12 iconst_1
13 iadd
14 putstatic
```

### 5. volatile 特性之指令禁重排

`https://tech.meituan.com/2014/09/23/java-memory-reordering.html`
`org.example.demo_7.Reordering`

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

## 8. CAS

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



