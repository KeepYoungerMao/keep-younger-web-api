package com.mao.entity.response;

import lombok.Getter;
import lombok.Setter;

/**
 * 错误信息返回体
 * @author zongx at 2020/4/7 22:06
 */
@Getter
@Setter
class ErrorData {

    private int code;
    private String message;
    private String path;
    private long timestamp;

    ErrorData(int code, String message, String path){
        this.code = code;
        this.message = message;
        this.path = path;
        this.timestamp = System.currentTimeMillis();
    }

}
