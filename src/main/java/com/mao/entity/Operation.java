package com.mao.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * 操作类型字段
 * @author zongx at 2020/4/7 22:59
 */
@Getter
@Setter
public class Operation {
    private Long update;        //更新时间
    private boolean delete;     //删除标识
}
