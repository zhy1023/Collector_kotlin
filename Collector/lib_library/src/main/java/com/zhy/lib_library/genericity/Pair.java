package com.zhy.lib_library.genericity;

/**
 * @author ； ZY
 * @date : 2020/9/27
 * @describe :
 */
public class Pair<U,V> {
    U first;
    V second;

    public Pair(U first, V second) {
        this.first = first;
        this.second = second;
    }

    public U getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "Pair{" +
                "first=" + first +
                ", second=" + second +
                '}';
    }
}
