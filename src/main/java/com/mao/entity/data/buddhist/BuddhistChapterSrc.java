package com.mao.entity.data.buddhist;

import lombok.Getter;
import lombok.Setter;

/**
 * 佛经章节详情
 * create by mao at 2020/4/15 22:27
 */
@Getter
@Setter
public class BuddhistChapterSrc extends BuddhistChapter {
    private Long pid;       //佛经id
    private String src;     //详情内容
}
