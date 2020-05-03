package com.mao.entity.data.book;

import com.mao.entity.Page;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 古籍查询参数
 * @author zongx at 2020/4/7 23:09
 */
@Getter
@Setter
@ToString
public class BookParam extends Page {

    private String name;            //名字关键词
    private String auth;            //作者关键词
    private Integer type;           //类型id
    private Integer dynasty;        //朝代id
    private Boolean free;           //是否免费
    private Boolean off_sale;       //是否下架

}
