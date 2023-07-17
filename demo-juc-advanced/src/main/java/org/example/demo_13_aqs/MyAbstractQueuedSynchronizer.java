package org.example.demo_13_aqs;

import sun.misc.Unsafe;

import java.io.Serializable;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;

public abstract class MyAbstractQueuedSynchronizer extends AbstractOwnableSynchronizer implements Serializable {
    static final class Node { // 封装的Node
        static final Node SHARED = new Node(); // 标志Node共享型
        static final Node EXCLUSIVE = null; // // 标志Node独占型
        static final int CANCELLED =  1; // waitStatus - 标志线程被取消
        static final int SIGNAL    = -1; // waitStatus - 标志后继线程需要 unparking
        static final int CONDITION = -2; // waitStatus - 标志线程等待condition
        static final int PROPAGATE = -3; // waitStatus - 标志下一个 acquireShared 无条件传播
        volatile int waitStatus;
        volatile Node prev; // predecessor
        volatile Node next; // successor
        volatile Thread thread;
        Node nextWaiter;
        final Node predecessor() throws NullPointerException {
            Node p = prev;
            if (p == null)
                throw new NullPointerException();
            else
                return p;
        }
        Node() { }
        Node(Thread thread, Node mode) {
            this.thread = thread;
            this.nextWaiter = mode;
        }
        Node(Thread thread, int waitStatus) {
            this.thread = thread;
            this.waitStatus = waitStatus;
        }
    }
    
    private transient volatile Node head; // 首元节点，无数据
    private transient volatile Node tail; // 尾节点
    private volatile int state; // 同步状态标志位 0-空闲 1-使用
    protected final int getState() {return state;}
    protected final void setState(int newState) {state = newState;}
    
    protected final boolean compareAndSetState(int expect, int update) {
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }
    
    protected boolean tryAcquire(int arg) {throw new UnsupportedOperationException();} // 模板钩子
    
    /*
      查询是否有任何线程等待获取的时间比当前线程的时间长
      如果当前线程之前有一个已排队的线程，则为 true；如果当前线程位于队列的头部或队列为空，则为 false
      调用此方法相当于（但可能比以下更有效）：getFirstQueuedThread() != Thread.currentThread() && hasQueuedThreads()
      请注意，由于中断和超时可能随时发生取消，因此 true 返回并不能保证其他线程会在当前线程之前获取。 同样，在该方法返回 false 后，由于队列为空，另一个线程也可能赢得排队竞争。
      该方法旨在由公平同步器使用以避免闯入。 此类同步器的 tryAcquire 方法应返回 false，并且如果该方法返回 true，则其 tryAcquireShared 方法应返回负值（除非这是可重入获取）。
     */
    public final boolean hasQueuedPredecessors() {
        Node t = tail;
        Node h = head;
        Node s;
        return h != t && ((s = h.next) == null || s.thread != Thread.currentThread());
    }
    
    public final void acquire(int arg) {
        if (!tryAcquire(arg) && acquireQueued(addWaiter(Node.EXCLUSIVE), arg)) {
            selfInterrupt();
        }
    }
    
    static void selfInterrupt() {
        Thread.currentThread().interrupt();
    }
    
    // 以给定模式为当前线程创建节点并将其排队
    private Node addWaiter(Node mode) {
        Node node = new Node(Thread.currentThread(), mode); // 创建当前线程的节点，准备排队
        Node pred = tail;
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        enq(node);
        return node;
    }
    // 节点尾插入队
    private Node enq(final Node node) {
        for (;;) {
            Node t = tail;
            if (t == null) {
                // 初始状态 tail==null
                // 初始化一个无数据的首元节点 tail = head = new Node()
                if (compareAndSetHead(new Node()))
                    tail = head;
            } else {
                // 非初始状态 tail!=null
                // 尾插法三步走
                // 1. 当前节点的前驱节点指向尾节点 2. 尾节点后移指向当前节点 3. 原先的尾节点的后继节点指向当前节点
                node.prev = t;
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }
    
    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) && parkAndCheckInterrupt()) {
                    interrupted = true;
                }
            }
        } finally {
            if (failed) {
                cancelAcquire(node);
            }
        }
    }
    
    private void setHead(Node node) {
        head = node;
        node.thread = null;
        node.prev = null;
    }
    
    // 检查并更新获取失败的节点状态。如果线程应该阻塞则返回 true。这是在所有采集循环中的主要信号控制。
    // 目的是将前置节点的 waitStatus 设置为 SIGNAL(-1) 以保证当前节点可以正确唤醒
    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
        int ws = pred.waitStatus;
        if (ws == Node.SIGNAL) {
            // 前驱节点的状态 waitStatus=SIGNAL 标识该节点已设置正确的状态，保证当前线程可以安全 park
            return true;
        }
        if (ws > 0) {
            // 前驱节点的状态 waitStatus=CANCELLED 标识该节点已被取消
            // 跳过前驱节点
            do {
                node.prev = pred = pred.prev;
            } while (pred.waitStatus > 0);
            pred.next = node;
        } else {
            // waitStatus=0 or CONDITION(-2) or PROPAGATE(-3) 一律设置为 SIGNAL(-1) 表明等待 park
            compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
        }
        return false;
    }
    
    // 前直节点 waitStatus=SIGNAL 在这里被阻塞 LockSupport.park(this);
    private final boolean parkAndCheckInterrupt() {
        LockSupport.park(this); // 真正被阻塞的操作
        return Thread.interrupted();
    }
    
    private void cancelAcquire(Node node) {
        if (node == null) {
            return;
        }
        node.thread = null;
        Node pred = node.prev;
        while (pred.waitStatus > 0) {
            node.prev = pred = pred.prev;
        }
        Node predNext = pred.next;
        node.waitStatus = Node.CANCELLED;
        if (node == tail && compareAndSetTail(node, pred)) {
            compareAndSetNext(pred, predNext, null);
        } else {
            int ws;
            if (pred != head &&
                        ((ws = pred.waitStatus) == Node.SIGNAL ||
                                 (ws <= 0 && compareAndSetWaitStatus(pred, ws, Node.SIGNAL))) &&
                        pred.thread != null) {
                Node next = node.next;
                if (next != null && next.waitStatus <= 0)
                    compareAndSetNext(pred, predNext, next);
            } else {
                unparkSuccessor(node);
            }
            node.next = node; // help GC
        }
    }
    
    private void unparkSuccessor(Node node) {
        int ws = node.waitStatus;
        if (ws < 0) {
            compareAndSetWaitStatus(node, ws, 0);
        }
        Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            s = null;
            for (Node t = tail; t != null && t != node; t = t.prev) {
                if (t.waitStatus <= 0) {
                    s = t;
                }
            }
        }
        if (s != null) {
            LockSupport.unpark(s.thread);
        }
    }
    
    public final boolean release(int arg) {
        if (tryRelease(arg)) {
            Node h = head;
            if (h != null && h.waitStatus != 0) {
                unparkSuccessor(h);
            }
            return true;
        }
        return false;
    }
    
    protected boolean tryRelease(int arg) {
        throw new UnsupportedOperationException();
    }
    
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long stateOffset;
    private static final long headOffset;
    private static final long tailOffset;
    private static final long waitStatusOffset;
    private static final long nextOffset;
    
    static {
        try {
            stateOffset = unsafe.objectFieldOffset(AbstractQueuedSynchronizer.class.getDeclaredField("state"));
            headOffset = unsafe.objectFieldOffset(AbstractQueuedSynchronizer.class.getDeclaredField("head"));
            tailOffset = unsafe.objectFieldOffset(AbstractQueuedSynchronizer.class.getDeclaredField("tail"));
            waitStatusOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("waitStatus"));
            nextOffset = unsafe.objectFieldOffset(Node.class.getDeclaredField("next"));
        } catch (Exception ex) { throw new Error(ex); }
    }
    private final boolean compareAndSetHead(Node update) {
        return unsafe.compareAndSwapObject(this, headOffset, null, update);
    }
    private final boolean compareAndSetTail(Node expect, Node update) {
        return unsafe.compareAndSwapObject(this, tailOffset, expect, update);
    }
    private static final boolean compareAndSetWaitStatus(Node node, int expect, int update) {
        return unsafe.compareAndSwapInt(node, waitStatusOffset, expect, update);
    }
    private static final boolean compareAndSetNext(Node node, Node expect, Node update) {
        return unsafe.compareAndSwapObject(node, nextOffset, expect, update);
    }
}
