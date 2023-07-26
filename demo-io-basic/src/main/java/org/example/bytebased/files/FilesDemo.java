package org.example.bytebased.files;

import java.io.File;

public class FilesDemo {
    public static void main(String[] args) {
        File file = new File("demo-io-basic/src/main/resources/files/randomaccess");
        System.out.println(file.exists());
    }
}
