package com.zjj.disk.utils;

import java.util.Arrays;
import java.util.List;

/**
 * @author zjj
 * @create 2023-03-26 20:15
 */
public class TypeUtil {
    //文本
    private static final List<String> document= Arrays.asList(".doc",".docx",".xls",".xlsx",".ppt",".pptx",".txt",".pdf");
    //图片
    private static final List<String> picture=Arrays.asList(".jpg",".png",".JPEG",".bmp",".gif");
    //视屏
    private static final List<String> video=Arrays.asList(".avi",".mp4",".rmvb",".flv",".wmv");
    //音频
    private static final List<String> audio=Arrays.asList(".mp3",".wav",".amr");
    //压缩
    private static final List<String> compress=Arrays.asList(".zip",".rar",".7z");

    public static String judgeType(String suf){
        String type = null;
        if (document.contains(suf)){
            type = "文本";
        }else if (picture.contains(suf)){
            type = "图片";
        }else if (video.contains(suf)){
            type = "视屏";
        }else if (audio.contains(suf)){
            type = "音频";
        }else if (compress.contains(suf)){
            type = "压缩";
        }else {
            type = "未知";
        }
        return type;
    }


}
