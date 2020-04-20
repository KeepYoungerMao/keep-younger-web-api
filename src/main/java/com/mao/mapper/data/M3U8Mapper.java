package com.mao.mapper.data;

import com.mao.entity.data.m3u8.Live;
import com.mao.entity.data.m3u8.Movie;
import com.mao.entity.data.m3u8.MovieParam;
import com.mao.entity.data.m3u8.MovieSrc;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 播放地址数据查询
 * @author : create by zongx at 2020/4/20 17:45
 */
@Mapper
public interface M3U8Mapper {

    /**
     * 获取所有电视播放地址
     * @return 电视列表
     */
    List<Live> getLives();

    /**
     * 根据参数获取电影列表
     * @param movieParam 参数
     * @return 电影列表
     */
    List<Movie> getMovies(MovieParam movieParam);

    /**
     * 根据参数获取电影总数
     * @param movieParam 参数
     * @return 总数
     */
    Long getMoviesTotalPage(MovieParam movieParam);

    /**
     * 根据参数查询电影列表
     * @param movieParam 参数
     * @return 电影列表
     */
    List<Movie> searchMovies(MovieParam movieParam);

    /**
     * 根据id查询电影详情
     * @param id 电影id
     * @return 电影详情
     */
    MovieSrc getMovie(@Param("id") long id);

}
