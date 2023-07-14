package org.example.demo_12;

import org.openjdk.jol.info.ClassLayout;

/*
1110100 10100001 01000100 10000010
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                               VALUE
      0     4        (object header)                           01 82 44 a1 (00000001 10000010 01000100 10100001) (-1589345791)
      4     4        (object header)                           74 00 00 00 (01110100 00000000 00000000 00000000) (116)
      8     4        (object header)                           e5 01 00 f8 (11100101 00000001 00000000 11111000) (-134217243)
     12     4        (loss due to the next object alignment)
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
 */
public class NoLock {
    public static void main(String[] args) {
        // 小端存储，64bit 高地址位置 存 高字节
        Object obj = new Object();
        // 调用才有
        System.out.println(Integer.toBinaryString(obj.hashCode()));
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
    }
}
