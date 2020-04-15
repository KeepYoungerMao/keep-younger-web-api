package com.mao.entity.data.bjx;

import com.mao.entity.Operation;
import lombok.Getter;
import lombok.Setter;

/**
 * 百家姓
 * create by mao at 2020/4/15 22:15
 */
@Getter
@Setter
public class Bjx extends Operation {
    private Long id;            //主键
    private String name;        //名称
    private String py;          //拼音首字母
}
