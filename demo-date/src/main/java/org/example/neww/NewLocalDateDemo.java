package org.example.neww;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// java.time.LocalTime
// java.time.LocalDate
// java.time.LocalDateTime
public class NewLocalDateDemo {
    public static void main(String[] args) {
        init();
        localDateApi();
        localDateTimeApi();
    }
    
    private static void init() {
        LocalTime localTime = LocalTime.now();
        LocalDate localDate = LocalDate.now();
        LocalDateTime localDateTime = LocalDateTime.now();
    
        System.out.println(localTime); // 16:12:49.581
        System.out.println(localDate); // 2020-09-23
        System.out.println(localDateTime); // 2020-09-23T16:12:49.581
    }
    
    // =====LocalDate=====
    private static void localDateApi() {
        LocalDate now = LocalDate.now();
        int year = now.getYear(); // 年
        int monthValue = now.getMonthValue(); // 月
        int dayOfMonth = now.getDayOfMonth(); // 日
        int dayOfYear = now.getDayOfYear(); // 一年中第几天
        int value = now.getDayOfWeek().getValue(); // 周几
    
        // 修改
        LocalDate localDate = now.withYear(2020);
        LocalDate localDate1 = now.withMonth(10);
        LocalDate localDate2 = now.withDayOfMonth(10);
        
        // 增加
        LocalDate localDate3 = now.plusYears(1);
        LocalDate localDate4 = now.plusMonths(1);
        LocalDate localDate5 = now.plusDays(1);
    
        // 减少
        LocalDate localDate6 = now.minusYears(1);
        LocalDate localDate7 = now.minusMonths(1);
        LocalDate localDate8 = now.minusDays(1);
        
        // 指定日期
        LocalDate localDate9 = LocalDate.of(2020, 10, 2);
    
        // 比较日期
        LocalDate day1 = LocalDate.of(2020, 10, 2);
        LocalDate day2 = LocalDate.of(2020, 10, 2);
        boolean equals1 = day1.equals(day2);
        boolean equals2 = day1.isAfter(day2);
        boolean before = day1.isBefore(day2);
        boolean equal = day1.isEqual(day2);
    }
    
    // =====LocalTime=====
    private static void localTimeApi() {
    }
    
    // =====LocalDateTime=====
    private static void localDateTimeApi() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear(); // 年
        int monthValue = now.getMonthValue(); // 月
        int dayOfMonth = now.getDayOfMonth(); // 日
        int dayOfYear = now.getDayOfYear(); // 一年中第几天
        int value = now.getDayOfWeek().getValue(); // 周几
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        int nano = now.getNano(); // 纳秒
        
        // 修改 with
        // 增加 plus
        // 减少 minus
        
        // 指定时间创建
        LocalDateTime of = LocalDateTime.of(2022, 10, 10, 11, 11, 11);
    
        LocalDateTime day1 = LocalDateTime.of(2022, 10, 10, 11, 11, 11);
        LocalDateTime day2 = LocalDateTime.of(2022, 10, 10, 11, 11, 11);
        boolean equals1 = day1.equals(day2);
        boolean equals2 = day1.isAfter(day2);
        boolean before = day1.isBefore(day2);
        boolean equal = day1.isEqual(day2);
        
        // 可以互相转换
        LocalDate localDate = of.toLocalDate();
        LocalTime localTime = of.toLocalTime();
        LocalDateTime of1 = LocalDateTime.of(localDate, localTime);
    }
}
