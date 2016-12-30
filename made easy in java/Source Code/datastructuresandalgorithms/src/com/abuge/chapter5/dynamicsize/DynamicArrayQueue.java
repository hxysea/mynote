package com.abuge.chapter5.dynamicsize;

import java.util.Arrays;

/**
 * Created by AbuGe on 2016/12/27.
 */
public class DynamicArrayQueue {
    private int[] queueRep;
    private int size;
    private int head;
    private int rear;

    private static int CAPACITY = 16;
    private static final int MIN_CAPACITY = 1 << 15;

    public DynamicArrayQueue() {
        queueRep = new int[CAPACITY];
        size = 0;
        head = 0;
        rear = 0;
    }

    public DynamicArrayQueue(int capacity) {
        queueRep = new int[capacity];
        size = 0;
        head = 0;
        rear = 0;
        CAPACITY = capacity;
    }

    public int getSize() {
        return size;
    }

    public boolean isFull() {
        return size == CAPACITY;
    }

    public boolean isEmpty() {
        return 0 == size;
    }

    public void enQueue(int data) {

        if (isFull()) {
            expand();
        }
        queueRep[rear] = data;
        rear++;
        size++;
    }

    public int deQueue() {
        if (isFull()) {
            throw new IllegalStateException("queue underflow exception: queue is empty.");
        }
        int data = queueRep[head];
        queueRep[head] = Integer.MIN_VALUE;
        head++;
        size--;//元素个数要减一
        return data;
    }

    private void expand() {

        int[] newQueue = new int[CAPACITY << 1];

        System.arraycopy(queueRep, head, newQueue, 0, size);
        head = 0;
        rear = size;
        queueRep = newQueue;
        CAPACITY = 2 * CAPACITY;
    }

    public void shrink() {
        if (CAPACITY <= MIN_CAPACITY || size << 2 >= CAPACITY) {
            return;
        }

        int[] newQueue = new int[CAPACITY / 2];
        System.arraycopy(queueRep, 0, newQueue, 0, size);
        queueRep = newQueue;
    }

    @Override
    public String toString() {
        return "DynamicArrayQueue{" +
                "queueRep=" + Arrays.toString(queueRep) +
                ", size=" + size +
                ", head=" + head +
                ", rear=" + rear +
                '}';
    }
}
