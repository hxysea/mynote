package com.abuge.chapter5.test;

import com.abuge.chapter5.dynamicsize.DynamicArrayQueue;

/**
 * Created by AbuGe on 2016/12/27.
 */
public class DynamicArrayQueueTest {
    public static void main(String[] args){
        DynamicArrayQueue dynamicArrayQueue = new DynamicArrayQueue(2);

        dynamicArrayQueue.enQueue(1);
        dynamicArrayQueue.enQueue(2);
        dynamicArrayQueue.enQueue(3);
        dynamicArrayQueue.enQueue(4);

        dynamicArrayQueue.enQueue(5);
        dynamicArrayQueue.enQueue(6);
        dynamicArrayQueue.enQueue(7);
        dynamicArrayQueue.enQueue(8);
        dynamicArrayQueue.enQueue(9);

        System.out.println(dynamicArrayQueue.toString());
        dynamicArrayQueue.shrink();
        System.out.println(dynamicArrayQueue.toString());
    }

}



