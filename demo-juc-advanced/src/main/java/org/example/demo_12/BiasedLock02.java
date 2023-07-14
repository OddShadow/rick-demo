package org.example.demo_12;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/*
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           05 a8 3f df (00000101 10101000 00111111 11011111) (-549476347)
      4     4        (object header)                           e9 01 00 00 (11101001 00000001 00000000 00000000) (489)
      8     4        (object header)                           e5 01 00 f8 (11100101 00000001 00000000 11111000) (-134217243)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
 */
public class BiasedLock02 {
    public static void main(String[] args) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Object obj = new Object();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable()); // 未有锁时，线程ID为0
        // 可以用 JNI 调用 C 获取线程 ID 对比一下
        System.out.println("=========");
        synchronized (obj) {
            System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        }
    }
}
