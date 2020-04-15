package com.mao.mapper.data;

import com.mao.entity.data.book.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 古籍数据请求
 * @author zongx at 2020/4/7 23:04
 */
@Mapper
public interface BookMapper {

    /**
     * 条件查询古籍列表
     * 分类过滤查找，可分页
     */
    List<Book> getBooks(BookParam bookParam);

    /**
     * 条件查询古籍列表
     * 查询总数
     */
    long getBookTotalPage(BookParam bookParam);

    /**
     * 条件查询古籍列表
     * 字符过滤查找，不可分页
     */
    List<Book> searchBooks(BookParam bookParam);

    /**
     * 根据id查询古籍详情
     * @param id id
     * @return 古籍详情
     */
    BookSrc getBookById(@Param("id") Long id);

    /**
     * 根据古籍id查询章节列表
     * @param id 古籍id
     * @return 章节列表
     */
    List<BookChapter> getBookChapterByBookId(@Param("id") Long id);

    /**
     * 根据id查询章节详情
     * @param id 章节id
     * @return 章节详情
     */
    BookChapterSrc getBookChapterById(@Param("id") Long id);

}
