package com.abuge.chapter5;

/**
 * Created by AbuGe on 2016/12/20.
 */
public class FixedSizeArrayQueueSketch {
    private int[] content = new int[1024];
    private int head;
    private int tail;

    public void enQueue(int data) {
        int length = content.length;
        content[tail] = data;
        tail++;
        if (tail == length && 0 != head) {
            tail = 0;
        }
    }

    public int deQueue() {
        int length = content.length;
        int headElement = content[head];
        content[head] = 0;
        head++;
        if (head == length) {
            head = 0;
        }
        return headElement;
    }

    public int getHead() {
        return content[0];
    }

    public boolean isEmpty() {
        return 0 == getQueueSize();
    }

    public int getQueueSize() {
        int length = content.length;
        int size = 0;

        for (int i = head; i < tail; i++) {
            if (0 != content[i]) {
                size++;
            } else {
                break;
            }
        }
        return size;
    }
}
