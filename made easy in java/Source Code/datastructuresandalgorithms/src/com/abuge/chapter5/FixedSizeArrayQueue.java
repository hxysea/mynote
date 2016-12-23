package com.abuge.chapter5;

/**
 * Created by AbuGe on 2016/12/22.
 */
public class FixedSizeArrayQueue {
    private int[] queueRep;
    private int front;
    private int rear;
    private int size;

    private static final int CAPACITY = 16;

    public FixedSizeArrayQueue() {
        queueRep = new int[CAPACITY];
        front = 0;
        rear = 0;
        size = 0;
    }

    public FixedSizeArrayQueue(int capacity) {
        queueRep = new int[capacity];
        front = 0;
        rear = 0;
        size = 0;
    }

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

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return 0 == size;
    }

    public boolean isFull() {
        int length = queueRep.length;

        return length == size;
    }

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
