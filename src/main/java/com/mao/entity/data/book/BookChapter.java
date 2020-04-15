package com.mao.entity.data.book;

import lombok.Getter;
import lombok.Setter;

/**
 * 古籍 章节
 * @author zongx at 2020/1/11 20:21
 */
@Getter
@Setter
public class BookChapter {
    private Long id;            //主键
    private Integer order;      //排序
    private String name;        //章节名称
}
