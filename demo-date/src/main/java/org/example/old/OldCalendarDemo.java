package org.example.old;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

// java.util.Calendar
public class OldCalendarDemo {
    public static void main(String[] args) {
        apiCalendar();
    }
    
    // Calendar.MONTH 很离谱的用 0-11 来代表 1-12月
    private static void apiCalendar() {
        Calendar now = Calendar.getInstance(); // 获取日历对象
        
        now.get(Calendar.DAY_OF_MONTH); // 获取某个域信息
        System.out.println(now.get(Calendar.DAY_OF_YEAR));
        System.out.println(now.get(Calendar.YEAR) + "-" + now.get(Calendar.MONTH) + "-" + now.get(Calendar.DAY_OF_MONTH));
        System.out.println(now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND));
        System.out.println(now.get(Calendar.MILLISECOND));
        
        Date date = now.getTime();// 获取日期对象
        String formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(date);
        System.out.println(formatDate);
    
        long timeInMillis = now.getTimeInMillis();// 获取自 1970-01-01 00:00:00 000 毫秒值
        System.out.println(timeInMillis);
        System.out.println(date.getTime());
    
        now.set(Calendar.YEAR, 2020); // 修改域信息
        now.set(Calendar.MONTH, 6); // 修改域信息
        now.set(Calendar.DAY_OF_MONTH, 1); // 修改域信息
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(now.getTime()));
        
        now.add(Calendar.DAY_OF_MONTH, -1); // 增加或减少域信息
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(now.getTime()));
    }
    
}
