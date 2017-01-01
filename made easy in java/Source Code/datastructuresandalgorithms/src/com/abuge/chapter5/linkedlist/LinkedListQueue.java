package com.abuge.chapter5.linkedlist;

/**
 * Created by AbuGe on 2017/1/1.
 */
public class LinkedListQueue {
    private LinkedNode head;
    private LinkedNode rear;
    private int length;

    public LinkedListQueue() {
        length = 0;
        head = null;
        rear = null;
    }

    public void enQueue(int data) {
        LinkedNode linkedNode = new LinkedNode(data);
        if (isEmpty()) {
            head = linkedNode;
        } else {
            rear.setNext(linkedNode);
        }
        rear = linkedNode;
        length++;
    }

    public int deQueue() {
        if (isEmpty()) {
            throw new IllegalStateException("underflow exception:queue is empty.");
        }
        int data = head.getData();
        LinkedNode nextNode = head.getNext();
        head.next = null;
        head = nextNode;
        length--;
        if (isEmpty()) {
            rear = null;//处理队列出队后，队列为空时的队尾置空
        }
        return data;
    }

    public int getLength() {
        return length;
    }

    public boolean isEmpty() {
        return 0 == length;
    }

    public int head() {
        if (isEmpty()) {
            throw new IllegalStateException("underflow exception:queue is empty.");
        }
        return head.getData();
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        LinkedNode front = head;
        result.append("LinkedQueue{");
        while (null != front) {
            result.append(front.getData());
            if (null != front.getNext()) {
                result.append(", ");
            }
            front = front.getNext();
        }
        result.append(isEmpty() ? "length = " + length : ", length = " + length);
        result.append("}");
        return result.toString();
    }

    private class LinkedNode {
        private int data;
        private LinkedNode next;

        public LinkedNode(int data) {
            this.data = data;
        }

        public void setNext(LinkedNode next) {
            this.next = next;
        }

        public LinkedNode getNext() {
            return next;
        }

        public int getData() {
            return data;
        }
    }
}
