package com.zhy.lib_library.thread;

/**
 * @author ； ZY
 * @date : 2020/9/21
 * @describe :
 */
public class ThreadTest {
    private static boolean isStop;

    static class ThreadA extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isStop) {
                System.out.println("Thread Name :" + this.getName());
            }
            System.out.println("ThreadA exist !");
        }
    }

    static class ThreadB extends Thread {
        @Override
        public void run() {
            super.run();
            isStop = true;
        }
    }

    public static void main(String[] args) throws Exception {
        Thread threadA = new ThreadA();
        threadA.start();
        Thread.sleep(100);
        new ThreadB().start();
        System.out.println("main thread exist !");

    }

}
