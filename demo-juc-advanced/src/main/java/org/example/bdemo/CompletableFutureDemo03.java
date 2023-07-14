package org.example.bdemo;

import org.example.bdemo.entity.Student;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class CompletableFutureDemo03 {
    
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(6);

        long startTime = System.currentTimeMillis();
        int generateNumber = 10000;
//        List<Student> students = generateRandomStudentWithCompletableFuture(threadPool, generateNumber);
        List<Student> students = generateRandomStudentWithoutCompletableFuture(generateNumber);
        long endTime = System.currentTimeMillis();
//        students.forEach(System.out::println);
        System.out.printf(">> 生成%d个对象 costTime=%05dms %n", generateNumber, endTime-startTime);
    
        withoutCompletableFuture(students);
        withCompletableFuture(students, threadPool);
        
        threadPool.shutdown();
    }
    
    private static void withoutCompletableFuture(List<Student> students) {
        long startTime = System.currentTimeMillis();
        List<String> resultList =
                students.stream()
                        .map(student -> String.format("年龄 %03d 的考生 [%s] 考取了 [%06.2f] 分", student.getAge(), student.getName(), student.getScore()))
                        .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
//        resultList.forEach(System.out::println);
        System.out.printf(">> withoutCompletableFuture\t顺序执行 costTime=%05dms %n", endTime-startTime);
    }
    
    private static void withCompletableFuture(List<Student> students, ExecutorService threadPool) {
        long startTime = System.currentTimeMillis();
        List<String> resultList =
                students.stream()
                        .map(student -> CompletableFuture.supplyAsync(() -> String.format("年龄 %03d 的考生 [%s] 考取了 [%06.2f] 分", student.getAge(), student.getName(), student.getScore()), threadPool))
                        .collect(Collectors.toList())
                        .stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList());
        long endTime = System.currentTimeMillis();
//        resultList.forEach(System.out::println);
        System.out.printf(">> withCompletableFuture\t并发执行 costTime=%05dms %n", endTime-startTime);
    }
    
    // >> 生成10000个对象 costTime=00257ms
    private static List<Student> generateRandomStudentWithCompletableFuture(ExecutorService threadPool, int number) {
        List<Student> randomList = Collections.synchronizedList(new ArrayList<>(number));
        for (int i = 0; i < 6; i++) {
            CompletableFuture.supplyAsync(() -> {
                List<Student> tempList = new ArrayList<>(number/3);
                for (int j = 0; j < number/6; j++) {
                    randomList.add(generateRandomStudent());
                }
                return tempList;
            }, threadPool).whenComplete((v, e) -> {
                randomList.addAll(v);
            });
        }
        return randomList;
    }
    
    // >> 生成10000个对象 costTime=00257ms
    private static List<Student> generateRandomStudentWithoutCompletableFuture(int number) {
        List<Student> randomList = new ArrayList<>(number);
        for (int j = 0; j < number; j++) {
            randomList.add(generateRandomStudent());
        }
        return randomList;
    }
    
    private static Student generateRandomStudent() {
        return new Student(
                UUID.randomUUID().toString().substring(0, 8),
                ThreadLocalRandom.current().nextInt(100),
                BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(300)).setScale(2, RoundingMode.FLOOR)
        );
    }
}
