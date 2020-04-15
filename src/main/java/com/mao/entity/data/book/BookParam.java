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

    private String name;
    private String auth;
    private Integer type;
    private Integer dynasty;
    private Boolean free;
    private Boolean off_sale;

}
