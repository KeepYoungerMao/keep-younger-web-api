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

    public static boolean isZs(Integer num){
        return null != num && num > 0;
    }

    public static boolean isNotZs(Integer num){
        return !isZs(num);
    }

    public static boolean isZs(Long num){
        return null != num && num > 0;
    }

    public static boolean isNotZs(Long num){
        return !isZs(num);
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

    public static void pf(int[][] arr){
        for (int[] as : arr) {
            for (int a : as) {
                System.out.print(a+",");
            }
            System.out.println();
        }
        System.out.println();
    }

    //二维数组的所有组合方式
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

    /**
     * url匹配
     * 1.将匹配规则按[*]切割
     * 2.依次匹配规则的部分，如果规则部分在url中有出现，则将url匹配上的部分截去，继续匹配下一个规则部分
     * 3.如果都匹配上，则匹配成功
     * 【使用String.indexOf()查找字符串中最先出现该字符串的位置】
     * 举例：
     * reg : / a / * / c / * . a
     * url : / a / c / a / c / a . a
     *
     * reg切割： / a /    / c /   . a
     *
     * 匹配： / a /   成功  / a / c / a / c / a . a   ->   截去【/ a /】        ->    c / a / c /a . a
     *                     ↑ ↑ ↑
     * 匹配： / c /   成功  c / a / c / a . a         ->   截去【c / a / c /】  ->    a . a
     *                           ↑ ↑ ↑
     * 匹配： . a     成功  a . a                     ->   截去【a . a】        ->    无
     *                     ↑ ↑ ↑
     * 匹配完毕
     * @param reg 匹配规则
     * @param url url
     * @return boolean
     */
    public static boolean match(String reg, String url){
        if ("".equals(reg))
            return "".equals(url) || "/".equals(url);
        if ("/*".equals(reg))
            return true;
        String[] reg_split = reg.split("\\*");
        int index = 0, reg_len = reg_split.length;
        boolean b = reg.charAt(reg.length() - 1) == '*';
        while (url.length() > 0) {
            if (index == reg_len)
                return b;
            String r = reg_split[index++];
            int indexOf = url.indexOf(r);
            if (indexOf == -1)
                return false;
            url = url.substring(indexOf + r.length());
        }
        return true;
    }

    public static void main(String[] args) {

    }

}
