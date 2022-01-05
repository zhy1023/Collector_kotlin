package com.zhy.lib_library.bean;

/**
 * @author ； ZY
 * @date : 2020/9/24
 * @describe :
 */
public class Person {
    private int age;
    private String name;
    public boolean isAlive;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    private void run() {
        System.out.println("run !");
    }

    public void eat() {
        System.out.println("eat !");

    }
}
