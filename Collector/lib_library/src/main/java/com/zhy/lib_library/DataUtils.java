package com.zhy.lib_library;

/**
 * @author ； ZY
 * @date : 2020/8/6
 * @describe : 排序算法
 */
public class DataUtils {

    /**
     * 冒泡排序
     *
     * @param arrs
     * @return
     */
    public static int[] sortByBubble(int[] arrs) {
        if (isEmptyArray(arrs)) return arrs;
        for (int i = 0; i < arrs.length; i++) {
            for (int j = 1; j < arrs.length - i; j++) {
                int temp;
                if (arrs[j] > arrs[j - 1]) {
                    temp = arrs[j];
                    arrs[j] = arrs[j - 1];
                    arrs[j - 1] = temp;
                }
            }
        }
        return arrs;
    }

    /**
     * 选择排序
     *
     * @param arrs
     * @return
     */
    public static int[] sortBySelect(int[] arrs) {
        if (isEmptyArray(arrs)) return arrs;
        for (int i = 0; i < arrs.length; i++) {
            int miniIndex = i;
            for (int j = i; j < arrs.length; j++) {
                if (arrs[j] < arrs[miniIndex])
                    miniIndex = j;
            }
            int temp = arrs[miniIndex];
            arrs[miniIndex] = arrs[i];
            arrs[i] = temp;
        }
        return arrs;
    }

    /**
     * 插入排序
     *
     * @param arrs
     * @return
     */
    public static int[] sortByInsert(int[] arrs) {
        if (isEmptyArray(arrs)) return arrs;
        int current;
        for (int i = 0; i < arrs.length - 1; i++) {
            current = arrs[i + 1];
            int preIndex = i;
            while (preIndex >= 0 && current < arrs[preIndex]) {
                arrs[preIndex + 1] = arrs[preIndex];
                preIndex--;
            }
            arrs[preIndex + 1] = current;
        }
        return arrs;
    }




    private static boolean isEmptyArray(int[] arrs) {
        return arrs == null || arrs.length == 0;
    }

}
