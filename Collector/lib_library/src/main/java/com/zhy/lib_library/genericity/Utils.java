package com.zhy.lib_library.genericity;

/**
 * @author ； ZY
 * @date : 2020/9/27
 * @describe :
 */
public class Utils {
    public static <T> int getIndex(T[] arr, T element) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == element) return i;
        }
        return -1;
    }

    public static <U, V> Pair<U, V> makePair(U first, V second) {
        return new Pair<>(first, second);
    }

    public static <T extends Comparable<T>> T max(T[] arr) {
        T max = arr[0];
        for (T t : arr) {
            if (t.compareTo(max) > 0) max = t;
        }
        return max;
    }

}
