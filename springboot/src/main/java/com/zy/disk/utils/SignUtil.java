package com.zy.disk.utils;

import java.util.Random;


public class SignUtil {
    private static final String BASIC = "123456789qwertyuiopasdfghjklzxcvbnm";

    public static String random(){
        char[] basicArray = BASIC.toCharArray();
        Random random = new Random();
        char[] result = new char[6];
        for (int i = 0; i < result.length; i++) {
            int index = random.nextInt(100) % (basicArray.length);
            result[i] = basicArray[index];
        }
        return new String(result);
    }
}

