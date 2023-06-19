package org.example.demojucqueue.task.abstart;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public abstract class AbstartDelayedObject implements Delayed {
    
    private final long delayTime;
    
    public AbstartDelayedObject(long delayTime) {
        this.delayTime = delayTime; // 延时时间加上当前时间
    }
    
    protected abstract void init();
    public abstract void doTask();
    
    // 获取延时任务的倒计时时间
    @Override
    public long getDelay(TimeUnit unit) {
        long diff = delayTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }
    
    // 延时任务队列，按照延时时间元素排序，实现Comparable接口
    @Override
    public int compareTo(Delayed obj) {
        return Long.compare(this.delayTime, ((AbstartDelayedObject) obj).delayTime);
    }
    
    @Override
    public String toString() {
        Date date = new Date(delayTime);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "OrderDelayObject:{"
                       + "time=" + sd.format(date)
                       + "}";
    }
}
