仅访问文件或者目录元数据，对于文件内容读取需要使用 
`FileOutputStream` `FileOutputStream` `RandomAccessFile`

创建 `File`  
`File file = new File("c:\\data\\test.txt")`  
传入的路径既可以是文件，也可以是目录

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