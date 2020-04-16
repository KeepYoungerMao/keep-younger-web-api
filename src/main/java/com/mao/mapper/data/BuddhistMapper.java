package com.mao.mapper.data;

import com.mao.entity.data.buddhist.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 佛经数据请求
 * @author : create by zongx at 2020/4/16 13:55
 */
@Mapper
public interface BuddhistMapper {

    /**
     * 根据过滤参数查询佛经列表
     * @param buddhistParam 过滤参数
     * @return 佛经列表
     */
    List<Buddhist> getBuddhists(BuddhistParam buddhistParam);

    /**
     * 根据过滤参数查询佛经总数
     * @param buddhistParam 过滤参数
     * @return 佛经总数
     */
    Long getBuddhistTotalPage(BuddhistParam buddhistParam);

    /**
     * 根据查询参数获取佛经列表
     * @param buddhistParam 查询参数
     * @return 佛经列表
     */
    List<Buddhist> searchBuddhists(BuddhistParam buddhistParam);

    /**
     * 根据id获取佛经详情
     * @param id 佛经id
     * @return 佛经详情
     */
    BuddhistSrc getBuddhist(@Param("id") Long id);

    /**
     * 根据佛经id获取章节列表
     * @param id 佛经id
     * @return 章节列表
     */
    List<BuddhistChapter> getChapters(@Param("id") Long id);

    /**
     * 根据章节id获取章节详情
     * @param id 章节id
     * @return 章节详情
     */
    BuddhistChapterSrc getChapter(@Param("id") Long id);

}
