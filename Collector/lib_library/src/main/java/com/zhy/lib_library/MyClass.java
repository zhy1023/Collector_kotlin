package com.zhy.lib_library;

public class MyClass {
    public static int index;

    public static void main(String[] args) {
        System.out.println("hello !");
        System.out.println("i = " + index);
        int[] arrs = new int[]{20, 14, 3, 18, 9, 22};
        DataUtils.sortByBubble(arrs);
//        DataUtils.sortBySelect(arrs);
//        DataUtils.sortByInsert(arrs);
        for (int i : arrs) {
            System.out.println("i :" + i);
        }
    }
}