package com.zjj.disk.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;

/**
 * @author zjj
 * @create 2023-03-26 15:18
 */
public class ValidUtil {
    public static HashMap<String,String> vaild(BindingResult result){
        HashMap<String, String> map = new HashMap<>();
        for (FieldError fieldError : result.getFieldErrors()) {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return map;
    }
}
