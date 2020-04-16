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
    private long total;          //总页数
    private int rows;           //每页条数
    private int page;           //当前页
    private Object filter;      //过滤参数
    private List<T> data;       //当前页数据

    public PageData(){}

    public PageData(long total,int rows, int page, Object filter, List<T> data){
        this.total = total;
        this.rows = rows;
        this.page = page;
        this.filter = filter;
        this.data = data;
    }
}
