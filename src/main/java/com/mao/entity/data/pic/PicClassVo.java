package com.mao.entity.data.pic;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * 图片分类展现数据
 * create by mao at 2020/4/17 0:00
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PicClassVo {
    private Long id;
    private String name;
    private List<PicClassVo> child;
}
