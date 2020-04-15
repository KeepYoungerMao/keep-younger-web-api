package com.mao.entity.data.buddhist;

import com.mao.entity.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * 佛经参数包装类
 * @author zongx at 2020/3/14 22:14
 */
@Getter
@Setter
public class BuddhistParam extends Page {

    private String name;        //名称
    private String auth;        //作者
    private Integer type;       //类型id

}
