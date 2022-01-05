package com.zhy.lib_library.data_struct;

import java.util.Stack;

/**
 * @Author ；zhy
 * @ClassName: StackData
 * @Date : 2021/2/2 14:23
 * @Describe : StackData 栈
 */
public class StackData {
    private Stack<Integer> stackData;
    private Stack<Integer> stackMin;

    public StackData() {
        stackData = new Stack<>();
        stackMin = new Stack<>();
    }

    public void push(int value) {
        stackData.push(value);
        if (stackMin.isEmpty()) {
            stackMin.push(value);
        } else {
            if (value < stackMin.peek())
                stackMin.push(value);
        }
    }

    public void pop() {
        if (stackData.isEmpty())
            throw new IllegalStateException("stack is empty!");
        int min = stackData.peek();
        if (min == stackMin.peek()) stackMin.pop();
        stackData.pop();
    }

    public int getMin() {
        if (stackMin.isEmpty())
            throw new IllegalStateException("stack is empty!");
        return stackMin.peek();
    }


}
