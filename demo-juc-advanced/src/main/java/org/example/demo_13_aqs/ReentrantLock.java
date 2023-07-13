package org.example.demo_13_aqs;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ReentrantLock implements Lock, Serializable {
    public ReentrantLock() {sync = new NonfairSync();} // 默认构造参数 非公平锁
    public ReentrantLock(boolean fair) {sync = fair ? new FairSync() : new NonfairSync();} // 构造参数 公平锁 非公平锁
    private final Sync sync; // 构造一个 sync 继承于 AbstractQueuedSynchronizer
    
    abstract static class Sync extends MyAbstractQueuedSynchronizer {
        abstract void lock();
        final boolean nonfairTryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
        protected final boolean tryRelease(int releases) {
            int c = getState() - releases;
            if (Thread.currentThread() != getExclusiveOwnerThread()) {
                throw new IllegalMonitorStateException();
            }
            boolean free = false;
            if (c == 0) {
                free = true;
                setExclusiveOwnerThread(null);
            }
            setState(c);
            return free;
        }
        protected final boolean isHeldExclusively() {
            return getExclusiveOwnerThread() == Thread.currentThread();
        }
//        final ConditionObject newCondition() {
//            return new ConditionObject();
//        }
        final Thread getOwner() {
            return getState() == 0 ? null : getExclusiveOwnerThread();
        }
        final int getHoldCount() {
            return isHeldExclusively() ? getState() : 0;
        }
        final boolean isLocked() {
            return getState() != 0;
        }
        private void readObject(java.io.ObjectInputStream s) throws java.io.IOException, ClassNotFoundException {
            s.defaultReadObject();
            setState(0); // reset to unlocked state
        }
    }
    
    // ========== 非公平锁 ==========
    static final class NonfairSync extends Sync {
        // 最关键的 lock 方法
        final void lock() {
            // CAS 尝试将 AbstractQueuedSynchronizer 的 status 0==>1
            if (compareAndSetState(0, 1)) {
                // 成功 exclusiveOwnerThread=当前线程 主要给第一个线程用
                setExclusiveOwnerThread(Thread.currentThread());
            } else {
                // 失败
                acquire(1);
            }
        }
        protected final boolean tryAcquire(int acquires) {
            return nonfairTryAcquire(acquires);
        }
    }
    // ========== 公平锁 ==========
    static final class FairSync extends Sync {
        // 最关键的 lock 方法
        final void lock() {
            /*
              1. 先 tryAcquire，判断队列中无Node或者自己是第一个元素，尝试 CAS 将 status 0==>1
                 成功 ==> exclusiveOwnerThread=当前线程，操作 status 0==>1
            */
            acquire(1);
        }
        protected final boolean tryAcquire(int acquires) {
            final Thread current = Thread.currentThread();
            int c = getState();
            if (c == 0) {
                if (!hasQueuedPredecessors() && compareAndSetState(0, acquires)) {
                    setExclusiveOwnerThread(current);
                    return true;
                }
            } else if (current == getExclusiveOwnerThread()) {
                int nextc = c + acquires;
                if (nextc < 0)
                    throw new Error("Maximum lock count exceeded");
                setState(nextc);
                return true;
            }
            return false;
        }
    }
    
    @Override public void lock() {sync.lock();} // 核心上锁方法
    @Override public void unlock() {sync.release(1);} // 核心释放锁方法
    
    @Override
    public void lockInterruptibly() throws InterruptedException {
    
    }
    
    @Override
    public boolean tryLock() {
        return false;
    }
    
    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }
    

    
    @Override
    public Condition newCondition() {
        return null;
    }

}
