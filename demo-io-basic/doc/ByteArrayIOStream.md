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
3. `mark()`

   