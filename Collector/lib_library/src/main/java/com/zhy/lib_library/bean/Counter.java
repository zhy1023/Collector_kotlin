package com.zhy.lib_library.bean;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ； ZY
 * @date : 2020/10/14
 * @describe :
 */
public  class Counter {

    private AtomicInteger cnt = new AtomicInteger();

    public  void add() {
        cnt.incrementAndGet();
    }

    public int get() {
        return cnt.get();
    }
}
