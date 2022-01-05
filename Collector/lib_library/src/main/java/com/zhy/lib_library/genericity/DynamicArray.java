package com.zhy.lib_library.genericity;

import java.util.Arrays;

/**
 * @author ； ZY
 * @date : 2020/9/27
 * @describe :
 */
public class DynamicArray<E> {
    private static final int DEFAULT_CAPACITY = 10;
    private int size;
    private Object[] elementData;

    public DynamicArray() {
        this.elementData = new Object[DEFAULT_CAPACITY];
    }

    private void ensureCapacity(int minCapacity) {
        int oldCapacity = elementData.length;
        if (oldCapacity >= minCapacity) return;
        int newCapacity = oldCapacity >> 1;
        if (newCapacity < minCapacity) newCapacity = minCapacity;
        elementData = Arrays.copyOf(elementData, newCapacity);
    }

    public void add(E e) {
        ensureCapacity(size + 1);
        elementData[size++] = e;
    }

    public E get(int index) {
        return (E) elementData[index];
    }

    public int size() {
        return size;
    }

    public E set(int index, E element) {
        E oldValue = get(index);
        elementData[index] = element;
        return oldValue;
    }


    public void addAll(DynamicArray<? extends E> c) {
        for (int i = 0; i < c.size; i++) {
            add(c.get(i));
        }
    }

    public static void main(String[] args) {
        System.out.println(Utils.getIndex(new Integer[]{1, 3, 5}, 3));
        System.out.println(Utils.makePair("zhy", 666).toString());


       /* DynamicArray<Pair<String, Integer>> array = new DynamicArray<>();
        for (int i = 0; i < 10; i++) {
            array.add(new Pair<>("test" + i, i));
        }
        int i = 0;
        for (; i < array.size(); i++) {
            System.out.println(array.get(i).toString());
        }*/
    }
}
