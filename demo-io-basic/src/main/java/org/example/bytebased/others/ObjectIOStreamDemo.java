package org.example.bytebased.others;

import org.example.bytebased.utils.Student;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ObjectIOStreamDemo {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Path path = Paths.get("demo-io-basic/src/main/resources/files/student");
        ObjectOutputStream os = new ObjectOutputStream(Files.newOutputStream(path));
        Student student = new Student("rick", 18, true);
        os.writeObject(student);
        os.close();
        
        ObjectInputStream is = new ObjectInputStream(Files.newInputStream(path));
        Student object = (Student) is.readObject();
        System.out.println(object);
        is.close();
    }
}