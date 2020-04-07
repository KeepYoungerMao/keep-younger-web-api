package com.mao.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分页数据
 * @author zongx at 2020/4/7 23:05
 */
@Getter
@Setter
public class PageData<T> {
    private int total;          //总页数
    private int rows;           //每页条数
    private int page;           //当前页
    private Object filter;      //过滤参数
    private List<T> data;       //当前页数据
}
