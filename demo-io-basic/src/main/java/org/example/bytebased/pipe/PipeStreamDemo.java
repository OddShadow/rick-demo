package org.example.bytebased.pipe;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

public class PipeStreamDemo {
    public static void main(String[] args) throws IOException {
        final PipedOutputStream output = new PipedOutputStream();
        final PipedInputStream  input  = new PipedInputStream(output);
        
        Thread thread1 = new Thread(() -> {
            try {
                output.write("Hello world, pipe!".getBytes());
                output.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        
        Thread thread2 = new Thread(() -> {
            try {
                int data = input.read();
                while(data != -1) {
                    System.out.print((char) data);
                    data = input.read();
                }
                input.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    
        thread1.start();
        thread2.start();
    }
}
