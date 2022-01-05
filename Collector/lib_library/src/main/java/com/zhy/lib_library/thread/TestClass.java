package com.zhy.lib_library.thread;

import com.zhy.lib_library.bean.User;

import java.lang.reflect.Field;

/**
 * @author ； ZY
 * @date : 2020/9/24
 * @describe :
 */
public class TestClass {

    public static void main(String[] args) {
//        Class<Void> cls = Void.class;
      /*  try {
            Class<?> cls = Class.forName("com.zhy.lib_library.bean.User");
            System.out.println("cls.getName() :" + cls.getName());
            System.out.println("cls.getSimpleName() :" + cls.getSimpleName());
            System.out.println("cls.getCanonicalName :" + cls.getCanonicalName());
            System.out.println("cls.getPackage :" + cls.getPackage());

            Field[] fields = cls.getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                System.out.println("field :" + field);
            }


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }*/
        ThreadA threadA = new ThreadA();
        threadA.start();
        threadA.interrupt();
    }


    static class ThreadA extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                System.out.println("thread A start !");
                sleep(2000);
            } catch (InterruptedException e) {
                System.out.println("thread A awake !");
                e.printStackTrace();
            }
        }
    }

}
