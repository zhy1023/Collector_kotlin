package com.zhy.lib_library.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ； ZY
 * @date : 2020/10/14
 * @describe :
 */
public class SynchronizedExample {

    private synchronized void fun() {
        for (int i = 1; i < 10; i++) {
            System.out.println("i = " + i);
        }
    }


    public static void main(String[] args) {
        final SynchronizedExample example = new SynchronizedExample();
        final SynchronizedExample example2 = new SynchronizedExample();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                example.fun();
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                example2.fun();
            }
        });

    }
}
