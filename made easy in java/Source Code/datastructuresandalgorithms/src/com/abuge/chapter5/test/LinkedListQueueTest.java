package com.abuge.chapter5.test;

import com.abuge.chapter5.linkedlist.LinkedListQueue;

/**
 * Created by AbuGe on 2017/1/1.
 */
public class LinkedListQueueTest {
    public static void main(String[] args){
        LinkedListQueue linkedListQueue = new LinkedListQueue();
        linkedListQueue.enQueue(20);
        linkedListQueue.enQueue(30);
        linkedListQueue.enQueue(40);
        linkedListQueue.enQueue(50);
        System.out.println(linkedListQueue.toString());
        System.out.println("queue is empty = " + linkedListQueue.isEmpty());

        linkedListQueue.deQueue();
        linkedListQueue.deQueue();
        linkedListQueue.deQueue();
        linkedListQueue.deQueue();
        System.out.println(linkedListQueue.toString());
        System.out.println("queue is empty = " + linkedListQueue.isEmpty());
        linkedListQueue.deQueue();
    }
}
