package com.mao.util;

/**
 * 字符串工具类
 * @author zongx at 2020/4/8 21:24
 */
public class SU {

    public static boolean isEmpty(String str){
        return null == str || str.length() <= 0;
    }

    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }

    public static String[] splitParam(String param){
        return param.trim().split("_");
    }

}
