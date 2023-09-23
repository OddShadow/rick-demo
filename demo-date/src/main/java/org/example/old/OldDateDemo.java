package org.example.old;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// java.util.Date
// java.text.SimpleDateFormat
public class OldDateDemo {
    public static void main(String[] args) {
        apiDate();
        afterTenSeconds();
        parseTimeString();
        System.out.println("=====compareTime=====");
        compareTime();
    }
    
    private static void apiDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS"); // 传入一个 pattern 表示用该格式格式化
        Date now = new Date(); // 当前时间
        System.out.println("now===" + sdf.format(now));
    
        Date date = new Date(1L); // 传入自 1970-01-01 00:00:00 000 毫秒值
        System.out.println("date===" + sdf.format(date));
    
        long time = date.getTime(); // 获取毫秒值
        System.out.println("time===" + time);
    
        date.setTime(1000L); // 1970-01-01 00:00:01 000
        System.out.println("date===" + sdf.format(date));
    }
    
    // 获取当前时间 10 秒之后的时间
    private static void afterTenSeconds() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        Date now = new Date();
        System.out.println("当前时间" + now);
        
        Date newDate = new Date(now.getTime() + (2 * 1000L)); // 创建新对象
        now.setTime(now.getTime() + (10 * 1000L)); // 使用旧对象
    
        System.out.println(sdf.format(newDate));
        System.out.println(sdf.format(now));
    }
    
    // 解析时间字符串
    private static void parseTimeString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = "2020-07-30 06:01:04";
        Date date;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(date));
    }
    
    // 时间对比
    private static void compareTime() {
        String time1 = "2000-01-02 01:03:05";
        String time2 = "2000-01-02 01:04:05";
        String time3 = "2000-01-02 01:03:7";
        String time4 = "2000-01-02 1:3:3";
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        Date date3 = null;
        Date date4 = null;
        try {
            date1 = sdf.parse(time1);
            date2 = sdf.parse(time2);
            date3 = sdf.parse(time3);
            date4 = sdf.parse(time4);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    
        System.out.println(date1.compareTo(date2));
        List<Date> list = new ArrayList<>();
        list.add(date1);
        list.add(date2);
        list.add(date3);
        list.add(date4);
        list.sort(Date::compareTo);
        list.forEach(x -> System.out.println(sdf.format(x)));
    }
}
