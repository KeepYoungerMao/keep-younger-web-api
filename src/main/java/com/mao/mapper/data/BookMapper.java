package com.mao.mapper.data;

import com.mao.entity.data.Book;
import com.mao.entity.data.BookParam;
import org.apache.ibatis.annotations.Mapper;

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

}
