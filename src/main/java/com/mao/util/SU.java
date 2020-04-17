package com.mao.util;

import java.util.Random;
import java.util.UUID;

/**
 * 字符串工具类
 * @author zongx at 2020/4/8 21:24
 */
public class SU {

    private static final char[] chars = new char[]{
            'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
            'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'
    };

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

    public static String uuid(){
        return UUID.randomUUID().toString();
    }

    public static String random(){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 32; i++) {
            sb.append(chars[random.nextInt(chars.length)]);
        }
        return sb.toString();
    }

    public static Long parse(String id){
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static long ceil(long z, int m){
        return (long) Math.ceil(z/(double)m);
    }

    public static void main(String[] args) {

    }

    //获取所有可能性
    private static int[][] combination(int[][] arr){
        int num = 1;
        for (int[] a : arr) {
            num *= a.length;
        }
        int[][] result = new int[num][arr.length];
        int arr_num = num;
        for (int k = 0; k < arr.length; k++) {
            int v_num = arr[k].length;
            arr_num = arr_num / v_num;
            int position = 0;
            for (int v : arr[k]) {
                int v_position = position;
                int count = num / v_num / arr_num;
                for (int j = 1; j <= count; j++){
                    for (int i = 0; i < arr_num; i++){
                        result[v_position + i][k] = v;
                    }
                    v_position += arr_num * v_num;
                }
                position += arr_num;
            }
        }
        return result;
    }

}
