package com.mao.entity.response;

import com.mao.util.JsonUtil;

/**
 * 统一数据返回构建
 * @author zongx at 2020/4/8 21:14
 */
public class Response {

    private static final String NOTFOUND = "request or resource not found, please check your request uri";
    private static final String NOT_ALLOWED = "method not allowed. please check your http method[GET, POST, PUT, DELETE...]";
    private static final String ERROR = "server error. we will pick it up and fix it asap.";
    private static final String PERMISSION = "no permission";

    public static <T> String ok(T data){
        return o(ResponseEnum.SUCCESS,data);
    }

    public static String permission(String msg){
        return o(ResponseEnum.PERMISSION,msg);
    }

    public static String permission(){
        return o(ResponseEnum.PERMISSION,PERMISSION);
    }

    public static String notFound(String msg){
        return o(ResponseEnum.NOTFOUND,msg);
    }

    public static String notFound(){
        return o(ResponseEnum.NOTFOUND,NOTFOUND);
    }

    public static String notAllowed(String msg){
        return o(ResponseEnum.NOT_ALLOWED,msg);
    }

    public static String notAllowed(){
        return o(ResponseEnum.NOT_ALLOWED,NOT_ALLOWED);
    }

    public static String error(String msg){
        return o(ResponseEnum.ERROR,msg);
    }

    public static String error(){
        return o(ResponseEnum.ERROR,ERROR);
    }

    private static <T> String o(ResponseEnum type, T data){
        return JsonUtil.obj2json(new ResponseData<>(type.getCode(),type.getDetail(),data));
    }

}
