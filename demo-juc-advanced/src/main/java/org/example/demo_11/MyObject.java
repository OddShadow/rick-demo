package org.example.demo_11;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/*
java.lang.Object object internals:
 OFFSET  SIZE   TYPE DESCRIPTION                                 VALUE
      0     4        (object header) Mark Word - 4/8Bytes        01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4        (object header) Mark Word - 4/8Bytes        00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4        (object header) Class Pointer - 4/8Bytes    e5 01 00 f8 (11100101 00000001 00000000 11111000) (-134217243)
     12     4        (loss due to the next object alignment) - Padding
Instance size: 16 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total

org.example.demo_11.CustomizedObject object internals:
 OFFSET  SIZE               TYPE DESCRIPTION                               VALUE
      0     4                    (object header)                           01 00 00 00 (00000001 00000000 00000000 00000000) (1)
      4     4                    (object header)                           00 00 00 00 (00000000 00000000 00000000 00000000) (0)
      8     4                    (object header)                           db 23 01 f8 (11011011 00100011 00000001 11111000) (-134143013)
     12     4                int CustomizedObject.age                      0
     16     4   java.lang.String CustomizedObject.name                     null
     20     4                    (loss due to the next object alignment)
Instance size: 24 bytes
Space losses: 0 bytes internal + 4 bytes external = 4 bytes total
 */
public class MyObject {
    public static void main(String[] args) {
        System.out.println(VM.current().details()); // Returns the informational details about the current VM mode
        System.out.println(VM.current().objectAlignment()); // Returns the object alignment
        
        Object obj = new Object();
        System.out.println(ClassLayout.parseInstance(obj).toPrintable());
    
        CustomizedObject customizedObject = new CustomizedObject();
        System.out.println(ClassLayout.parseInstance(customizedObject).toPrintable());
    }
}
