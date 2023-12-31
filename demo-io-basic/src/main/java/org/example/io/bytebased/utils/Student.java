package org.example.io.bytebased.utils;

import java.io.Serializable;

public class Student implements Serializable {
    private String name;
    private Integer age;
    private boolean sex;
    
    public Student() {
    }
    
    public Student(String name, Integer age, boolean sex) {
        this.name = name;
        this.age = age;
        this.sex = sex;
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
    
    public boolean isSex() {
        return sex;
    }
    
    public void setSex(boolean sex) {
        this.sex = sex;
    }
    
    @Override
    public String toString() {
        return "Student{" +
                       "name='" + name + '\'' +
                       ", age=" + age +
                       ", sex=" + sex +
                       '}';
    }
}
