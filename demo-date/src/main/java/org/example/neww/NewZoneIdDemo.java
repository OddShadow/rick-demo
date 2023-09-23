package org.example.neww;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Set;
import java.util.TimeZone;

// java.time.ZoneId
// java.time.ZonedDateTime
// 时区id用 洲名/城市名 或者 国家名/城市名
public class NewZoneIdDemo {
    public static void main(String[] args) {
        zoneIdApi();
    }
    
    private static void zoneIdApi() {
        ZoneId zoneId = ZoneId.systemDefault(); // 获取系统默认时区 Asia/Shanghai
        Set<String> availableZoneIds = ZoneId.getAvailableZoneIds(); // 获取所有时区
        ZoneId of = ZoneId.of("America/New_York"); // 根据时区id封装ZoneId
        
        ZonedDateTime zonedDateTime1 = ZonedDateTime.now(); // 返回系统默认时区时间
        ZonedDateTime zonedDateTime2 = ZonedDateTime.now(Clock.systemUTC()); // 返回世界标准时间
        ZonedDateTime zonedDateTime3 = ZonedDateTime.now(ZoneId.of("America/New_York")); // 返回指定时区的时间
    
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone(of)); // 老API
    }
}
