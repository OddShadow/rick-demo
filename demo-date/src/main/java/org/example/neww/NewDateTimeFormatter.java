package org.example.neww;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class NewDateTimeFormatter {
    public static void main(String[] args) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss nnnnnnnnn");
        System.out.println(dtf.format(LocalDateTime.now()));
        System.out.println(DateTimeFormatter.ofPattern("n").format(Instant.now())); // 格式化器调用方法
        System.out.println(dtf.format(ZonedDateTime.now()));
    
        System.out.println(LocalDateTime.now().format(dtf)); // LocalDateTime 调用方法
        
        LocalDateTime parse = LocalDateTime.parse("2020-01-02 11:12:10 000000000", dtf);
        System.out.println(parse.format(dtf));
    }
}
