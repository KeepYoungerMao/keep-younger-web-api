package com.mao.entity.response;

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
class ResponseData<T> {

    private int code;
    private String message;
    private T data;

}
