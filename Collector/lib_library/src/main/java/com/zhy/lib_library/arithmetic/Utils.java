package com.zhy.lib_library.arithmetic;

import com.zhy.lib_library.bean.LinkNode;

/**
 * @Author ；zhy
 * @ClassName: Utils
 * @Date : 2020/12/29 15:22
 * @Describe : Utils
 */
public class Utils {

    public static int findIndex(int[] numArray, int index) {
        int left = 0;
        int right = numArray.length - 1;
        int mid = 0;
        while (left < right) {
            mid = (left + right) >> 1;
            if (numArray[mid] == index) return mid;
            if (numArray[mid] < index) left = mid;
            if (numArray[mid] > index) right = mid;
        }

        return -1;
    }


    /**
     * TODO 确定一个字符串 s 的所有字符是否全都不同。
     */
    public static boolean isUniqueStr(String str) {
        boolean isUniqueStr = false;
        /*   for (int i = 0; i < str.length(); i++) {
            for (int j = i + 1; j < str.length(); j++) {
                if (str.charAt(i) == str.charAt(j)) return true;
            }
        }*/
        for (int i = 0; i < str.length() - 1; i++) {
            char c = str.charAt(i);
            if (str.indexOf(c) != str.lastIndexOf(c)) {
                isUniqueStr = true;
                break;
            }
        }
        return isUniqueStr;
    }


    /**
     * TODO 字符串压缩。
     * <p>
     * 利用字符重复出现的次数，编写一种方法，实现基本的字符串压缩功能。比如，字符串aabcccccaaa会变为a2b1c5a3。
     * 若“压缩”后的字符串没有变短，则返回原先的字符串。你可以假设字符串中只包含大小写英文字母（a至z）。
     */
    public static String compressStr(String str) {
        if (null == str || str.length() < 3) return str;
        StringBuilder stringBuilder = new StringBuilder();
        char index = str.charAt(0);
        int count = 1;
        for (int i = 1; i <= str.length(); i++) {
            if (i == str.length()) {
                stringBuilder.append(str.charAt(i - 1)).append(count);
                break;
            }
            if (str.charAt(i) == index) {
                count++;
            } else {
                stringBuilder.append(index).append(count);
                index = str.charAt(i);
                count = 1;
            }
        }
        return stringBuilder.toString().length() > str.length() ? str : stringBuilder.toString();
    }


    /**
     * TODO 一次编辑
     * <p>
     * 字符串有三种编辑操作:插入一个字符、删除一个字符或者替换一个字符。 给定两个字符串，编写一个函数判定它们是否只需要一次(或者零次)编辑。
     * 输入:
     * first = "pale"
     * second = "ple"
     * 输出: True
     * <p>
     * 输入:
     * first = "pales"
     * second = "pal"
     * 输出: False
     */
    public static boolean oneEditAway(String first, String second) {
        if (null == first || null == second) return false;
        if (Math.abs(first.length() - second.length()) > 1) return false;
        if (first.length() == second.length()) {
            if (first.length() < 2) return true;
            else return isSame(first, second);
        }
        return isContain(first.length() < second.length() ? first : second, first.length() < second.length() ? second : first);
    }

    private static boolean isSame(String first, String second) {
        int count = 0;
        for (int i = 0; i < first.length(); i++) {
            if (first.charAt(i) != second.charAt(i)) count++;
        }
        return count < 2;
    }

    private static boolean isContain(String first, String second) {
        boolean isOneEditAway = true;
        for (int i = 0; i < first.length(); i++) {
            if (first.charAt(i) != second.charAt(i)) {
                if (second.charAt(i + 1) != first.charAt(i)) {
                    isOneEditAway = false;
                    break;
                }
            }
        }
        return isOneEditAway;
    }


    /**
     * TODO 回文排列
     * <p>
     * 给定一个字符串，编写一个函数判定其是否为某个回文串的排列之一。
     * <p>
     * 回文串是指正反两个方向都一样的单词或短语。排列是指字母的重新排列。
     * <p>
     * 回文串不一定是字典当中的单词。
     * <p>
     *  
     * <p>
     * 示例1：
     * <p>
     * 输入："tactcoa"
     * 输出：true（排列有"tacocat"、"atcocta"，等等）
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/palindrome-permutation-lcci
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     */
    public static boolean canPermutePalindrome(String s) {
        if (null == s) return false;
        if (s.length() == 1) return true;
        if (s.length() == 2) return s.charAt(0) == s.charAt(1);
        boolean isPalindromeStr = true;
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            int charCount = 0;
            for (int j = 0; j < s.length(); j++) {
                if (s.charAt(j) == s.charAt(i)) {
                    charCount++;
                }
            }
            if (charCount == s.length()) break;
            if (charCount == 1) count++;
            if (count > 1) {
                isPalindromeStr = false;
                break;
            }
        }
        return isPalindromeStr;
    }

    /**
     * 反转链表
     *
     * @param header
     * @return
     */
    public static LinkNode reverseNode(LinkNode header) {
        if (header == null || header.next == null) return header;
        LinkNode next = header.next;
        LinkNode newHeader = reverseNode(next);
        header.next = null;
        next.next = header;
        return newHeader;
    }
}
