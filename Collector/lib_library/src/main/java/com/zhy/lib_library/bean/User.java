package com.zhy.lib_library.bean;

import javax.jws.soap.SOAPBinding;

/**
 * @author ； ZY
 * @date : 2020/9/24
 * @describe :
 */
public class User extends Person {
    private int age;
    private String name;
    private User user;
    public int height;

    public User(int age, String name) {
        this.age = age;
        this.name = name;
    }

    private void look() {

    }

    private void play(User user) {
        this.user = user;
        System.out.println("play game !");
    }


}
