package com.mao.entity.data.buddhist;

import lombok.Getter;
import lombok.Setter;

/**
 * 佛经章节
 * create by mao at 2020/4/15 22:26
 */
@Getter
@Setter
public class BuddhistChapter {
    private String id;          //章节id
    private Integer order;      //排序
    private String title;       //标题
}
