package com.zhy.lib_library.data_struct;

/**
 * @Author ；zhy
 * @ClassName: ListNode 链表
 * @Date : 2020/12/24 17:23
 * @Describe :
 */
public class ListNode {
    public int val;
    public ListNode next;

    public ListNode(int x) {
        val = x;
    }

    @Override
    public String toString() {
        return "ListNode{" +
                "val=" + val +
                ", next=" + next +
                '}';
    }
}
