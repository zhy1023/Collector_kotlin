package com.zhy.lib_library.arithmetic;

import com.zhy.lib_library.data_struct.ListNode;

/**
 * @Author ；zhy
 * @ClassName: LinkUtils
 * @Date : 2021/1/29 17:25
 * @Describe : LinkUtils 链表相关
 */
public class LinkUtils {

    /**
     * 实现一种算法，删除单向链表中间的某个节点（即不是第一个或最后一个节点），假定你只能访问该节点。
     * 示例：
     * 输入：单向链表a->b->c->d->e->f中的节点c
     * 结果：不返回任何数据，但该链表变为a->b->d->e->f
     *
     * @param node
     */
    public static void deleteNode(ListNode node) {
        node = node.next;
        node.next = node.next.next;
    }



}
