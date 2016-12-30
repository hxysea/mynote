package com.abuge.chapter5.fixedsize;

/**
 * Created by AbuGe on 2016/12/22.
 */
public class FixedSizeArrayQueue {
    //存储队列数据的数组
    private int[] queueRep;
    private int front;
    private int rear;
    private int size;
    //实现队列的数组长度
    private static final int CAPACITY = 16;

    //使用默认长度初始化队列数组及队列首尾索引和队列长度
    public FixedSizeArrayQueue() {
        queueRep = new int[CAPACITY];
        front = 0;
        rear = 0;
        size = 0;
    }

    //使用给定长度初始化队列数组
    public FixedSizeArrayQueue(int capacity) {
        queueRep = new int[capacity];
        front = 0;
        rear = 0;
        size = 0;
    }

    //在队尾插入一个元素，时间复杂度为O(1)
    public void enQueue(int data) {
        int capacity = queueRep.length;
        if (size == capacity) {
            throw new IllegalStateException("Queue is full:Overflow");
        } else {
            size++;
            queueRep[rear] = data;
            rear = (rear + 1) % capacity;
        }
    }

    //从队首移除元素，时间复杂度为O(1)
    public int deQueue() {
        int capacity = queueRep.length;
        if (0 == size) {
            throw new IllegalStateException("Queue is empty: Underflow");
        } else {
            size--;
            int data = queueRep[front];
            queueRep[front] = Integer.MIN_VALUE;
            front = (front + 1) % capacity;
            return data;
        }
    }

    //返回队列元素个数，时间复杂度为O(1)
    public int size() {
        return size;
    }

    //校验队列是否为空，时间复杂度为O(1)
    public boolean isEmpty() {
        return 0 == size;
    }

    //校验队列是否已满，时间复杂度为O(1)
    public boolean isFull() {
        int length = queueRep.length;

        return length == size;
    }

    //队列元素字符串表示，时间复杂度为O(n)
    public String toString() {
        StringBuilder result = new StringBuilder("[");
        int length = queueRep.length;
        for (int i = 0; i < size; i++) {
            result.append(queueRep[(front + i) % length]);
            if (i < (size - 1)) {
                result.append(", ");
            }
        }
        result.append("]");
        return result.toString();
    }

}
