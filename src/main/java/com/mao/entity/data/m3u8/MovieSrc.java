package com.mao.entity.data.m3u8;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 电影详情
 * create by mao at 2020/4/19 21:44
 */
@Getter
@Setter
public class MovieSrc extends Movie {
    private String intro;
    private String m3u8;
    private List<MovieM3u8> urls;
}
