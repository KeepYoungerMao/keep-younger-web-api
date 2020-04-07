package com.mao.entity.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 错误类型
 * @author zongx at 2020/4/7 22:09
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {

    SUCCESS(200,"ok"),

    PERMISSION(401,"no permission"),

    NOTFOUND(404,"resource not found"),

    ERROR(500,"request error");

    private int code;
    private String detail;

}
