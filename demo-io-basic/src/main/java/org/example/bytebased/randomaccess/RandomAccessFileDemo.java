package org.example.bytebased.randomaccess;

import java.io.IOException;
import java.io.RandomAccessFile;

public class RandomAccessFileDemo {
    public static void main(String[] args) throws IOException {
        RandomAccessFile rw = new RandomAccessFile("demo-io-basic/src/main/resources/files/randomaccess", "rw");
        rw.seek(5);
        long filePointer = rw.getFilePointer();
    }
}
