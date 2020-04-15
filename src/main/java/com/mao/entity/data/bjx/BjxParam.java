package com.mao.entity.data.bjx;

import com.mao.entity.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * 百家姓查询参数
 * create by mao at 2020/4/15 22:18
 */
@Getter
@Setter
public class BjxParam extends Page {
    private String name;    //名称
    private String py;      //拼音首字母
}
