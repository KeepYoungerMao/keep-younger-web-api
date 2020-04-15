package com.mao.entity.data.book;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 古籍详情
 * create by mao at 2020/4/15 21:52
 */
@Getter
@Setter
public class BookSrc extends Book {
    private String image;               //图片
    private String intro;               //详情
    private List<BookChapter> chapters; //章节列表
}
