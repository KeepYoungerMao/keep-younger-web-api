package com.mao.entity.data.m3u8;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 电影放映地点类型
 * create by mao at 2020/4/19 22:12
 */
@Getter
@AllArgsConstructor
public enum MoviePlaceEnum {
    y(3002,"英国"),
    h(2002,"韩国"),
    r(2003,"日本"),
    z(1001,"中国大陆"),
    m(2001,"美国"),
    x(1002,"中国香港"),
    q(9999,"其他"),
    j(3003,"加拿大"),
    e(2004,"俄罗斯"),
    t(3004,"泰国"),
    w(1003,"中国台湾"),
    d(3001,"印度"),
    p(4005,"新加坡"),
    f(2005,"法国"),
    b(3005,"西班牙"),
    a(4001,"澳大利亚"),
    l(4002,"马来西亚"),
    c(4003,"爱尔兰"),
    i(4004,"荷兰"),
    k(3006,"意大利"),
    n(3007,"葡萄牙"),
    g(2006,"德国");

    private int id;
    private String name;

}
