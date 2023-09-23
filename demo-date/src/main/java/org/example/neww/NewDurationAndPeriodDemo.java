package org.example.neww;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class NewDurationAndPeriodDemo {
    public static void main(String[] args) {
        
        // 计算时间间隔
        LocalDateTime of1 = LocalDateTime.of(2022, 10, 10, 11, 11, 11);
        LocalDateTime of2 = LocalDateTime.of(2021, 10, 10, 11, 11, 11);
        Duration duration = Duration.between(of2, of1);
        System.out.println(duration.toDays());
        System.out.println(duration.toHours());
        System.out.println(duration.toMinutes());
        System.out.println(duration.getSeconds());
        System.out.println(duration.toMillis());
        System.out.println(duration.toNanos());
    
        // 计算日期间隔
        Period period = Period.between(LocalDate.of(2021, 10, 10), LocalDate.of(1992, 10, 10));
        int years = period.getYears();
        int months = period.getMonths(); // 这个间隔不跨年
        int days = period.getDays(); // 这个间隔不跨年，月
    
        long aDays = ChronoUnit.DAYS.between(LocalDate.of(2021, 10, 10), LocalDate.of(1992, 10, 10));
    }
}
