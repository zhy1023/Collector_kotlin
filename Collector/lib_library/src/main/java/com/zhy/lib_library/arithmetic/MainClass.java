package com.zhy.lib_library.arithmetic;


import com.zhy.lib_library.data_struct.ListNode;
import com.zhy.lib_library.data_struct.StackData;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @Author ；zhy
 * @ClassName: MainClass
 * @Date : 2020/12/29 15:21
 * @Describe : MainClass
 */
public class MainClass {

    public static void main(String[] args) {
//        System.out.println("isUniqueStr :" + Utils.isUniqueStr("asfcdea"));
//        System.out.println("compressStr :" + Utils.compressStr("aaaafcccccdeeeeh"));
//        System.out.println("oneEditAway :" + Utils.oneEditAway("pales","pal"));
//        System.out.println("canPermutePalindrome :" + Utils.canPermutePalindrome("aab"));

/*        ListNode node = new ListNode(1);
        node.next = new ListNode(2);
        System.out.println(node.toString());*/

        StackData stackData = new StackData();
        stackData.push(4);
        stackData.push(5);
        stackData.push(3);
        stackData.push(2);
        System.out.println("stackMin :" + stackData.getMin());

//        LinkedList<Integer> list = new LinkedList<>();
    }
}
