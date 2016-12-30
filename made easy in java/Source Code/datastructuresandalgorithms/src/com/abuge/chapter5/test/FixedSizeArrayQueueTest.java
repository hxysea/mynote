package com.abuge.chapter5.test;

/**
 * Created by AbuGe on 2016/12/27.
 */
public class FixedSizeArrayQueueTest {
    public static void main(String[] args){
        shrink();
    }

    private static void shrink(){
        int len = -10;
        if (len <= 1 << 15 || len << 2 >= len){
            System.out.println("直接返回， len = " + len);
            return;
        }

        if (len < 1 << 15){
            len = 1 << 15;
        }
        System.out.println("len = " + len);
    }
}
