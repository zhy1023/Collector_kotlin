package com.zhy.lib_library.thread;


public class MyClass {
    private static final String TAG = "MyClass";
    static int i;

    public static void main(String[] args) throws Exception {
        ThreadA threadA = new ThreadA();
        ThreadB threadB = new ThreadB();
        ThreadC threadC = new ThreadC();
        threadA.start();
        threadA.join();
        threadB.start();
//        threadB.join();
        threadC.start();
    }

    private static void operate() {

    }

    static class ThreadA extends Thread {
        @Override
        public void run() {
            super.run();
            System.out.println("Thread A start !!");
            operate();
        }
    }

    static class ThreadB extends Thread {
        @Override
        public void run() {
            super.run();
            System.out.println("Thread B start !!");
        }
    }

    static class ThreadC extends Thread {
        @Override
        public void run() {
            super.run();
            System.out.println("Thread C start !!");
        }
    }
}