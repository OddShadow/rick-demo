# learn-juc

## 1. JUC 基础

### 1.1 基本概念

1.1.2 Thread.State
1. NEW  
2. RUNNABLE  
3. BLOCKED  
4. WAITING  
5. TIMED_WAITING  
6. TERMINATED  

1.1.2 sleep / wait
1. sleep 是 Thread 静态方法, wait 是 Object 方法
2. sleep 不会释放锁也不需要占用锁 wait 会释放锁，但是调用它的前提是当前线程占有锁，即代码要在 synchronized 中
3. 它们都可以被 interrupted 方法中断

### 1.2 Lock接口

1.2.1 synchronized 关键字
1. 同步代码块
2. 修饰方法
3. 修饰静态方法
4. 修饰一个类

### 1.3 线程通信

1.3.1 线程间通信
1.3.2 线程间通信

### 1.4 集合多线程不安全问题

1. ArrayList HashSet HashMap 是线程不安全的

2. 对于 ArrayList 可以使用

    `new Vector<>()` 

    `Collections.synchronizedList(new ArrayList<>())` 

    `new CopyOnWriteArrayList<>()`

3. 一般使用 JUC 提供的 
   new CopyOnWriteArrayList<>(); 
   new CopyOnWriteArraySet<>(); 
   new ConcurrentHashMap<>();

### 1.5 Synchronized
1. 标准访问 先发短信还是邮件
2. 停4秒在短信方法内， 先打印短信还是邮件
3. 新增普通方法hello，先短信还是 hello
4. 有两部手机 先短信还是邮件
5. 两个静态同同步方法 1部手机 先打印短信还是邮件
6. 两个静态同步方法 2部手机 先打印短信还是邮件
7. 1个静态同步方法 1个普通方法 1部手机 先短信还是邮件

