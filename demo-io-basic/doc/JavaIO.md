# Java IO

https://jenkov.com/tutorials/java-io/index.html

## Overview

### 数据源

数据源根据角度不同分为两种
一种叫做 source 表示从该数据源中读取数据
一种叫做 destination 表示写入数据到该数据源

Stream 分为两种
一种 byte based
一种 character based

Source => InputStream/Reader => Program
Program => OutputStream/Writer => Destination

常见数据源

1. Files

2. Pipes

3. Network Connections

4. In-memory Buffers

5. System.in System.out System.error

## Files

常用 Java 类

```java
import java.io.File; // 文件信息和目录
import java.io.RandomAccessFile; // 非连续访问，可以 Random，其实就是 Arbitrarily 的访问
import java.io.FileInputStream; // byte based 读文件，连续访问
import java.io.FileOutputStream; // byte based 写文件，连续访问
import java.io.FileReader; // character based 读文件，连续访问
import java.io.FileWriter; // character based 写文件，连续访问
```

## Streams

## Pipes

## Networking