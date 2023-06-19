package org.example.demojucqueue.task;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时订单任务
 */
@Data
public class OrderDelayObject implements Delayed {
    
    private final String name;
    private final long delayTime;   // 延时时间
    private final String order; // 实际业务中这里传订单信息对象，我这里只做demo，所以使用字符串了
    
    public OrderDelayObject(String name, long delayTime, String order) {
        this.name = name;
        this.delayTime = System.currentTimeMillis() + delayTime; // 延时时间加上当前时间
        this.order = order;
    }
    
    // 获取延时任务的倒计时时间
    @Override
    public long getDelay(TimeUnit unit) {
        long diff = delayTime - System.currentTimeMillis();
        return unit.convert(diff, TimeUnit.MILLISECONDS);
    }
    
    // 延时任务队列，按照延时时间元素排序，实现Comparable接口
    @Override
    public int compareTo(Delayed obj) {
        return Long.compare(this.delayTime, ((OrderDelayObject) obj).delayTime);
    }
    
    @Override
    public String toString() {
        Date date = new Date(delayTime);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        return "OrderDelayObject:{"
                       + "name=" + name
                       + ", time=" + sd.format(date)
                       + ", order=" + order
                       + "}";
    }
    
}
