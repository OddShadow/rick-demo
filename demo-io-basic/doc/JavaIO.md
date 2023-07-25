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

## InputStream & OutputStream

`InputStream` 和 `OutputStream` 是抽象类，也是 Byte Based 的基础类

`InputStream` 主要方法，也是子类普遍方法

1. `int read()` 读并返回单个字节整型值
2. `int read(byte[] buffer)` 读字节到 buffer 中，返回实际读到的字节数，最后一次读完返回 -1
3. `int read(byte[] buffer, int offset, int length)` 指定 buffer 可用下标和长度
4. `byte[] readAllBytes()` Java 9 之后，读所有后返回字节数组
5. `boolean markSupported()` 当前类是否支持 `mark()` `reset()` 功能
6. `mark()` 标记当前读到位置，`mark=pos`
7. `reset()` 将读位置设置成最近一次 `mark()` 的位置，`pos=mark`
8. `int available()` 还有多少字节可读
9. `long skip(long n)` 跳过指定数量字节不读
10. `void close()` 关闭流

`OutputStream` 主要方法，也是子类普遍方法

1. `void write(int data)` 写一个字节
2. `void write(byte[])` 写整个 `byte[]` 
3. `void write(byte[], int offset, int length)` 写指定下标和长度的 `byte[]`
4. `void flush()` 刷新内存到硬盘中，防止缓存未持久化
5. `void close()` 关闭流

## FileInputStream & FileOutputStream

`FileInputStream`

1. `FileInputStream(String path)` 传入文件路径字符串创建
2. `FileInputStream(File file)` 传入 File 创建

`FileOutputStream`

1. `FileOutputStream(String path) ` 传入文件路径字符串创建，默认 overwrite 文件
2. `FileOutputStream(String path, boolean append) ` 传入文件路径字符串创建，可选 overwrite 或者 append
3. `FileOutputStream(File file)` 传入 File 创建，默认 overwrite 文件
4. `FileOutputStream(File file)` 传入 File 创建，可选 overwrite 或者 append

## RandomAccessFile

用于文件随机访问

读写模式
`r` 仅读
`rw` 可读可写
`rwd` 可读可写，文件内容立刻持久化
`rws` 可读可写，文件内容和 meta data 立刻持久化

1. `RandomAccessFile(String path, String mode)` 传入文件路径字符串，指定读写模式
2. `RandomAccessFile(File file, String mode)` 传入文件路径字符串，指定读写模式

常用方法

1. `void seek(long pos)` 设置文件位置指针
2. `long getFilePointer()` 获取文件位置指针

## File

用于文件/目录元数据信息处理，不涉及文件内容处理

创建 File 类不代表文件/目录已经存在 `File file = new File("/test")`

1. `boolean exists()`
检查文件或者目录是否存在
2. `boolean mkdir()` 
如果父级目录存在，指定目录不存在，则创建对应的目录，返回ture，其它情况都返回false
3. `boolean mkdirs()` 
如果指定目录不存在，则递归创建，返回ture，其它情况返回false
4. `long length()` 
读取文件内容长度，不支持目录
5. `boolean renameTo(File dest)` 
重命名或移动文件/目录
6. `boolean delete()`
删除一个文件/目录为空的目录
7. `boolean isDirectory()` 
检查路径是文件还是目录
8. `String[] list()` & `File[] listFiles()` 
读取目录中的文件列表

## PipedInputStream & PipedOutputStream

用户同一个 JVM 中不同线程间通信，很少用

## ByteArrayInputStream & ByteArrayOutputStream

`ByteArrayInputStream` 内置一个 `byte[] buffer` 引用作为数据源，实际的 buffer 是构造函数传入的

1. `ByteArrayInputStream(byte buf[])` 传入一个 buffer 作为数据源
2. `ByteArrayInputStream(byte buf[], int offset, int length)` 传入一个指定下标长度的 buffer 作为数据源

`ByteArrayOutputStream` 内置一个 `byte[] buffer` 作为数据源

1. `new ByteArrayOutputStream()`
2. `ByteArrayOutputStream(int size)` 指定 内置 buffer 大小
3. `byte[] toByteArray()` 拿到内置 buffer 数据源，即写入数据

## FilterInputStream & FilterOutputStream

过滤器，用处不大

## BufferedInputStream & BufferedOutputStream

1. `BufferedInputStream(InputStream in)` 创建一个缓冲流
2. `BufferedInputStream(InputStream in, int size)` 创建一个指定大小的缓冲流
3. `BufferedOutputStream(OutputStream out)`
4. `BufferedOutputStream(OutputStream out, int size)`

note: 这个流的缓存并不复用

## PushbackInputStream

## SequenceInputStream

## DataInputStream & DataOutputStream

