package com.mao.entity.data;

import lombok.Getter;
import lombok.Setter;

/**
 * 古籍查询参数
 * @author zongx at 2020/4/7 23:09
 */
@Getter
@Setter
public class BookParam {

    private String name;
    private String auth;
    private Integer type;
    private Integer dynasty;
    private Boolean free;
    private Boolean off_sale;

}
