# `ByteArrayInputStream`

## 创建流

1. 整个字节数组
   `new ByteArrayInputStream(byte[] byte)`
2.  部分字节数组
   `new ByteArrayInputStream(byte[] byte, int offset, int length)`

## 方法

1. `read()` `read(int byte[])` `read(int byte[], int offset, int length)`

2. `int available()`
   返回还剩多少字节未读

3. `void mark(int readlimit)`
   标记当前读到的字节位置，参数 `readlimit` 只是为了满足抽象类，在源码中被忽略了

4. `void reset()`
   只能在调用了 `mark()` 方法后调用，将从标记位置重新读

5. `long skip(long n)`
   参数想跳过的字节数，返回实际跳过的字节数

   

# ByteArrayOutputStream

创建流

1. `new ByteArrayOutputStream()`

方法

1. `void write(int b)` & `void write(byte b[])` & `void write(byte[] b, int offset, int length)`
2. `byte[] toByteArray()`