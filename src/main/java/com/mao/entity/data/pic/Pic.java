package com.mao.entity.data.pic;

import com.mao.entity.Operation;
import lombok.Getter;
import lombok.Setter;

/**
 * 图片列表数据
 * create by mao at 2020/4/16 23:56
 */
@Getter
@Setter
public class Pic extends Operation {
    private Long id;
    private String name;
    private Integer prl;
    private Integer srl;
    private Long pid;
    private Long sid;
    private String s_image;
}
