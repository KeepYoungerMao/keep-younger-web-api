package com.mao.service.log;

import io.vertx.ext.web.RoutingContext;

/**
 * 日志处理
 * vertX的Router处理中，Route是顺序进行的
 * 由于我在最前面添加了对所有请求的跨域处理和权限控制。
 * 所以所有请求都会经过filter方法，并且RoutingContext
 * @author : create by zongx at 2020/5/22 11:28
 */
public interface LogService {

    static LogService create(){
        return new LogServiceImpl();
    }

    void log(RoutingContext ctx);

}
