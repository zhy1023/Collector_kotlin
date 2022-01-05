package com.zhy.lib_library.thread;

import com.zhy.lib_library.bean.Counter;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author ； ZY
 * @date : 2020/10/14
 * @describe :
 */
public class AtomicExample {


    public static void main(String[] args) throws Exception {
        final int threadSize = 1000;
        final Counter counter = new Counter();
        final CountDownLatch downLatch = new CountDownLatch(threadSize);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < threadSize; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    counter.add();
                    downLatch.countDown();
                }
            });
        }
        downLatch.await();
        executorService.shutdown();
        System.out.println(counter.get());

    }
}
