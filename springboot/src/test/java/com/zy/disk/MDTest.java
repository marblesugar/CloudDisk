package com.zy.disk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.zy.disk.utils.MD5Util;


@SpringBootTest
public class MDTest {
    @Test
    public void testMD5(){
        String s = MD5Util.MD5("123456");
        System.out.println(s);

    }
}
