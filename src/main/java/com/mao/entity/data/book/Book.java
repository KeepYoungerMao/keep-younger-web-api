package com.mao.entity.data.book;

import com.mao.entity.Operation;
import lombok.Getter;
import lombok.Setter;

/**
 * 古籍
 * @author zongx at 2020/4/7 23:00
 */
@Getter
@Setter
public class Book extends Operation {
    private Long id;                //主键
    private String name;            //名称
    private String auth;            //作者
    private String s_image;         //小图片
    private String type;            //类型
    private Integer type_id;        //类型id
    private String dynasty;         //朝代
    private Integer dynasty_id;     //朝代id
    private Integer count;          //阅读频率
    private Boolean free;           //是否免费
    private Boolean off_sale;       //是否下架
}
