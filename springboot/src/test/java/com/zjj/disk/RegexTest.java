package com.zjj.disk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;

/**
 * @author zjj
 * @create 2023-04-09 14:24
 */
@SpringBootTest
public class RegexTest {
    @Test
    public void testRegex() throws InterruptedException {
        String patten = "^(?!.*(\\/)|(\\\\))";
        boolean matches = Pattern.matches(patten, "\\/\\");
        System.out.println(matches);

    }
}
