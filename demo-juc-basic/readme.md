# Java 并发案例

## 1. Synchronized 和 ReentrantLock

> 多线程编写一般步骤
>
> 1. 创建共享资源类，定义域和方法
> 2. 资源类中的方法一般三步 判断 执行 通知
> 3. 创建多个线程调用共享资源

1. 案例一 `ademo` `Synchronized` 和  `ReentrantLock` 实现同步
2. 案例二 `bdemo` `Synchronized` 和  `ReentrantLock` 实现线程间通信
   - [注意] 在线程阻塞时需要用 `while` 判断，不可以用 `if` 防止虚假唤醒
3. 案例三 `cdemo` `Synchronized` 和  `ReentrantLock` 实现线程间定制化通信

## 2. 集合的线程安全

案例 `ddemo`

```java
List<Object> list = new ArrayList<>(); // 线程不安全
Vector<Object> vector = new Vector<>(); // 线程安全
CopyOnWriteArrayList<Object> copyOnWriteArrayList = new CopyOnWriteArrayList<>(); // 线程安全
        
Set<Object> set = new HashSet<>(); // 线程不安全
CopyOnWriteArraySet<Object> copyOnWriteArraySet = new CopyOnWriteArraySet<>(); // 线程安全
    
Map<Object, Object> hashMap = new HashMap<>(); // 线程不安全
ConcurrentHashMap<Object, Object> concurrentHashMap = new ConcurrentHashMap<>(); // 线程安全

// 并发过程中没有同步导致可能出现的两个异常
// java.util.ConcurrentModificationException
// java.lang.IndexOutOfBoundsException
```

## 3. Synchronized 详细

1. 对于普通同步方法，锁的是当前实例对象
2. 对于静态同步方法，锁的施当前类的 Class 对象
3. 对于同步代码块，锁的是 `synchronized` 括号里面的对象