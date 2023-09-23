# Java 时间相关

## JDK8 之前

常用 `java.util.Date` `java.text.SimpleDateFormat` `java.util.Calendar`

需要改进的点

1. `Date` 中 `getYear()` 获取的是 1900 到如今的差值，而不是今年具体值
2. `Calendar` 中 `Calendar.getInstance().get(Calendar.MONTH)` 月份获取之后还要 + 1，因为用 0-11 代表 1-12月份
3. 精度只到 ms
4. 时间是可变对象，可修改，易丢失原始值
5. 线程不安全

## JDK8 之后

1. `LocalDate` 年月日
   `LocalTime` 时分秒
   `LocalDateTime` 年月日时分秒
   `ZoneId` 时区
   `ZonedDateTime` 带时区的时间

   用于替代 老 `Calendar` 类

2. `Instant` 时间戳

   用于替代 老 `Date` 类

3. `DateTimeFormatter` 时间格式化 解析

   用于替代 老 `SimpleDateFormat` 类

4. `Duration` 时间间隔 时分秒纳秒
   `Period` 时间间隔 年月日