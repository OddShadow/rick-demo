# RandomAccessFile

对比下 `FileOutputStream` 和 `FileInputStream`, 可以实现文件随机访问

创建方式

`new RandomAccessFile("c:\\data\\file.txt", "rw");`  
其中第二个参数代表访问模式
1. r-读模式, 调用 write 方法将抛出异常  
2. rw-读写模式  
3. rwd-同步读写模式，文件内容更新同步写入磁盘  
4. rws-同步读写模式, 文件内容和 meta data 更新同步写入磁盘

常用方法

1. `void seek(long pos)`  
移动指针
2. `long getFilePointer()`  
获取指针
3. `read()`  
有很多 `read` 相关方法，其中单个字节读取时，会自动将指针移动到下一个字节处、
4. `write()`