package org.example.demo01completablefuture;

import org.example.demo01completablefuture.entity.Student;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

// CompletableFuture
public class CompletableFutureDemo03 {
    public static List<Student> studentList = new ArrayList<>();
    static {
        studentList.add(new Student("rick", 2900, new BigDecimal("1.99")));
        studentList.add(new Student("marry", 4100, new BigDecimal("3.65")));
        studentList.add(new Student("jack", 3100, new BigDecimal("2.47")));
    }
    public static final int THREAD_NUMBER = 3;
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_NUMBER);
        withoutCompletableFuture(studentList);
        withCompletableFuture(studentList, threadPool);
        threadPool.shutdown();
    }
    
    private static void withoutCompletableFuture(List<Student> students) {
        long startTime = System.currentTimeMillis();
        List<String> resultList =
                students.stream()
                        .map(student -> {
                            String format = String.format("年龄 %03d 的考生 [%s] 考取了 [%06.2f] 分", student.getAge(), student.getName(), student.getScore());
                            System.out.println(format);
                            return format;
                        })
                        .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        System.out.printf(">> withoutCompletableFuture\t顺序执行 costTime=%05dms %n", endTime-startTime);
    }
    
    private static void withCompletableFuture(List<Student> students, ExecutorService threadPool) {
        long startTime = System.currentTimeMillis();
        List<String> resultList =
                students.stream()
                        .map(student -> CompletableFuture.supplyAsync(() -> {
                            String format = String.format("年龄 %03d 的考生 [%s] 考取了 [%06.2f] 分", student.getAge(), student.getName(), student.getScore());
                            System.out.println(format);
                            return format;
                        }, threadPool))
                        .collect(Collectors.toList())
                        .stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
        System.out.printf(">> withCompletableFuture\t并发执行 costTime=%05dms %n", endTime-startTime);
    }
}
