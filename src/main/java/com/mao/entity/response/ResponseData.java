package com.mao.entity.response;

import com.mao.util.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * 数据统一返回体
 * @author zongx at 2020/4/7 22:05
 */
@Getter
@Setter
@AllArgsConstructor
public class ResponseData<T> {

    private int code;
    private String message;
    private T data;

    public static <T> String ok(T data){
        return o(ResponseEnum.SUCCESS,data);
    }

    public static String permission(String message,String path){
        return o(ResponseEnum.PERMISSION,new ErrorData(ResponseEnum.PERMISSION.getCode(),message,path));
    }

    public static String notFound(String path){
        return o(ResponseEnum.NOTFOUND,new ErrorData(ResponseEnum.NOTFOUND.getCode(),"resource "+path+" not found",path));
    }

    public static String error(String message, String path){
        return o(ResponseEnum.ERROR,new ErrorData(ResponseEnum.ERROR.getCode(),message,path));
    }

    private static <T> String o(ResponseEnum type, T data){
        return JsonUtil.obj2json(new ResponseData<>(type.getCode(),type.getDetail(),data));
    }

}
