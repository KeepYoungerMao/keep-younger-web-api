package com.mao.entity.data.pic;

import com.mao.entity.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * 图片查询参数
 * create by mao at 2020/4/16 23:54
 */
@Getter
@Setter
public class PicParam extends Page {
    private String name;
    private Long pid;
    private Long sid;
}
