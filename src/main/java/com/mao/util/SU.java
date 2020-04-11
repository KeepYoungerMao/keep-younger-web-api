package com.mao.util;

import java.util.UUID;

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

    public static boolean isDigit(String str){
        for (char c : str.toCharArray()) {
            if (!(c >= '0' && c <= '9'))
                return false;
        }
        return true;
    }

    public static boolean isNumber(String str){
        return isNumber(str,0,Long.MAX_VALUE);
    }

    public static boolean isNumber(String str, long min, long max){
        try {
            long i = Long.parseLong(str);
            return i >= min && i <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isIP(String ip){
        if (isEmpty(ip))
            return false;
        String[] split = ip.split("[.]");
        if (split.length != 4)
            return false;
        for (String s : split) {
            if (!isNumber(s,0,255))
                return false;
        }
        return true;
    }

    public static String random(){
        return UUID.randomUUID().toString();
    }

}
