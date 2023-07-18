package org.example.demo01completablefuture.entity;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;

public class Student {
    
    private String name;
    private Integer age;
    private BigDecimal score;
    
    public Student() {
    }
    
    public Student(String name, Integer age, BigDecimal score) {
        this.name = name;
        this.age = age;
        this.score = score;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public BigDecimal getScore() {
        try {
            TimeUnit.MILLISECONDS.sleep(age);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return score;
    }
    
    public void setScore(BigDecimal score) {
        this.score = score;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                       "name='" + name + '\'' +
                       ", age=" + age +
                       ", score=" + score +
                       '}';
    }
}
