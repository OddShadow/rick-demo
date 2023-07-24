# Java OutputStream

## OutputStream

OutputStream 抽象类主要有以下几个实现类

```java
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.PipedOutputStream;
import java.io.BufferedOutputStream;
import java.io.FilterOutputStream;
import java.io.DataOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
```

OutputStream 写字节流，顺序写但是无序号  

主要方法有
1. `void write(int b)`  
传参表示写单个字节的整型值
2. `void write(byte[] b)` 和 `void write(byte[] b, int off, int len)`  
传入一个字节数组缓存区
3. `flush()`  
写入数据可能在内存中，没有写入硬盘持久化，调用 `flush()` 立刻持久化
4. `close()`

读取性能上使用 byte[] 比 byte 快可达10倍，取决于硬件缓存，数组不宜过大过小

记得关闭 stream 或使用 try 新用法自动关闭
```java
try(OutputStream outputStream = new FileOutputStream("file.txt")) {
// todo
}
```

## FileOutputStream

常用创建方式
   ```java
   new FileOutputStream(String path);
   new FileOutputStream(File file);
   new FileOutputStream(String path, boolean append);
   new FileOutputStream(File file, boolean append);
   ```

常用方法
1. `void write(int b)` & `void write(byte[] b)` & `void write(byte[] b, int off, int len)`
2. `flush()`