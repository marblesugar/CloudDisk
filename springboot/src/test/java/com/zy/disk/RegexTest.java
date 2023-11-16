package com.zy.disk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.regex.Pattern;

import static java.lang.Thread.sleep;


@SpringBootTest
public class RegexTest {
    @Test
    public void testRegex() throws InterruptedException {
        String patten = "^(?!.*(\\/)|(\\\\))";
        boolean matches = Pattern.matches(patten, "\\/\\");
        System.out.println(matches);

    }
}
