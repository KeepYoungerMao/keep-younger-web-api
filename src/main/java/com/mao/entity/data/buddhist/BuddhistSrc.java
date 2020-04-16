package com.mao.entity.data.buddhist;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 佛经详情
 * create by mao at 2020/4/15 22:24
 */
@Getter
@Setter
public class BuddhistSrc extends Buddhist {
    private String intro;                   //介绍
    private List<BuddhistChapter> chapters; //章节列表
}
