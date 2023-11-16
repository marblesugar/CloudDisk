package com.zjj.disk;

import com.zjj.disk.utils.MD5Util;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zjj
 * @create 2023-03-26 16:26
 */
@SpringBootTest
public class MDTest {
    @Test
    public void testMD5(){
        String s = MD5Util.MD5("123456");
        System.out.println(s);

    }
}
