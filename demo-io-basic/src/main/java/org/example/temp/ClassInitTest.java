package org.example.temp;

public class ClassInitTest {
    
    static {
        a = 4;
    }
    
    public static int a = 6;
    
    public static void main(String[] args) {
    
    
        String extDirs = System.getProperty("java.ext.dirs");
        for (String path : extDirs.split(";")) {
            System.out.println(path);
        }
    
    }
    
}
