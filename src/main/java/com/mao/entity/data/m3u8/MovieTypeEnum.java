package com.mao.entity.data.m3u8;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 电影类型
 * create by mao at 2020/4/19 22:35
 */
@Getter
@AllArgsConstructor
public enum MovieTypeEnum {
    a(1,"剧情片"),
    b(2,"恐怖片"),
    c(3,"爱情片"),
    d(4,"动作片"),
    e(5,"喜剧片"),
    f(6,"战争片"),
    g(7,"纪录片"),
    h(8,"科幻片");

    private int id;
    private String name;
}
