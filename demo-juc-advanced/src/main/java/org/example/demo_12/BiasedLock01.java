package org.example.demo_12;

import org.openjdk.jol.info.ClassLayout;

/*
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           05 98 3b 51 (00000101 10011000 00111011 01010001) (1362860037)
      4     4        (object header)                           af 01 00 00 (10101111 00000001 00000000 00000000) (431)
      8     4        (object header)                           e5 01 00 f8 (11100101 00000001 00000000 11111000) (-134217243)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
 */
//-XX:+UseBiasedLocking -XX:BiasedLockingStartupDelay=0 或者延迟 4秒后在进入锁
public class BiasedLock01 {
    public static void main(String[] args) {
        Object obj = new Object();
        synchronized (obj) {
            System.out.println(ClassLayout.parseInstance(obj).toPrintable());
        }
    }
}