package com.mao.entity.data;

import lombok.Getter;
import lombok.Setter;

/**
 * 古籍
 * @author zongx at 2020/4/7 23:00
 */
@Getter
@Setter
public class Book {
    private Long id;
    private String name;
    private String auth;
    private String image;
    private String s_image;
    private String intro;
    private String type;
    private Integer type_id;
    private String dynasty;
    private Integer dynasty_id;
    private Integer count;
    private Boolean free;
    private Boolean off_sale;
}
