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

    public static String permission(String path){
        return o(ResponseEnum.PERMISSION,new ErrorData(ResponseEnum.PERMISSION.getCode(),PERMISSION,path));
    }

    public static String notFound(String path){
        return o(ResponseEnum.NOTFOUND,new ErrorData(ResponseEnum.NOTFOUND.getCode(),NOTFOUND,path));
    }

    public static String notAllowed(String path){
        return o(ResponseEnum.NOT_ALLOWED,new ErrorData(ResponseEnum.NOT_ALLOWED.getCode(),NOT_ALLOWED,path));
    }

    public static String error(String path){
        return o(ResponseEnum.ERROR,new ErrorData(ResponseEnum.ERROR.getCode(),ERROR,path));
    }

    public static String error(String message, String path){
        return o(ResponseEnum.ERROR,new ErrorData(ResponseEnum.ERROR.getCode(),message,path));
    }

    private static <T> String o(ResponseEnum type, T data){
        return JsonUtil.obj2json(new ResponseData<>(type.getCode(),type.getDetail(),data));
    }

}
