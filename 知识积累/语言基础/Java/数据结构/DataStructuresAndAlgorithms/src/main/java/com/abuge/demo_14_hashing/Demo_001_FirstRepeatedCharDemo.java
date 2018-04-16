package com.abuge.demo_14_hashing;

/**
 * Created by huxianyang on 2018/4/13.
 */
public class Demo_001_FirstRepeatedCharDemo {
    public static void main(String[] args) {
        char[] str = {'a', 'b', 'a', 'd', 'd', 'a'};
        getFirstRepeatedChar(str);
    }

    private static char getFirstRepeatedChar(char[] str) {
        int[] charCount = new int[256];
        for (char c : str) {
            if (1 == charCount[c]) {
                System.out.println("'" + c + "' is the first repeated char...");

                return c;
            }
            charCount[c] = charCount[c] + 1;
        }

        System.out.println("there is no repeated char...");

        return 0;
    }
}
