# Java InputStream

## InputStream

InputStream 抽象类主要有以下几个实现类

```java
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.io.PipedInputStream;
import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.PushbackInputStream;
import java.io.DataInputStream;
import java.io.ObjectInputStream;
import java.io.SequenceInputStream;
```

InputStream 读字节流，顺序读但是无序号

主要方法有
1. `int read()`  
返回读到的字节的整型值，返回-1表示文件读完，对于ASCII字符可以直接强转 char
2. `int read(byte[])`  
传入一个字节数组缓存区，返回读到byte[]里的数量，返回-1表示文件读完，读完最后一次byte[]会残留上次读到的缓冲
3. `byte[] readAllBytes()`  
Java9新方法，传入一个byte[]，可以直接一次读完  
4. `boolean markSupport()` 和 `mark()` 和 `reset()`
5. `close()`

读取性能上使用 byte[] 比 byte 快可达10倍，取决于硬件缓存，数组不宜过大过小

记得关闭 stream 或使用 try 新用法自动关闭
```java
try(InputStream inputstream = new FileInputStream("file.txt")) {
// todo
}
```

这里文件分隔符, windows 系统中 上用 '\', Java 中需要转义, 所以 '\\'  
UNIX 系统中可以用 '/'

## FileInputStream

常用创建方式
   ```java
   new FileInputStream(String path);
   new FileInputStream(File file);
   ```
   
常用方法
1. `int read()` & `int read(byte[])` & `int read(byte[], int offset, int len)`