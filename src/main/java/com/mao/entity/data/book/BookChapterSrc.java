package com.mao.entity.data.book;

import lombok.Getter;
import lombok.Setter;

/**
 * 古籍 章节详情
 * create by mao at 2020/4/15 22:00
 */
@Getter
@Setter
public class BookChapterSrc extends BookChapter {
    private Long book_id;       //书本id
    private String content;     //章节内容
}
