package com.mao.service.data;

import com.mao.config.MybatisConfig;
import com.mao.entity.PageData;
import com.mao.entity.data.m3u8.*;
import com.mao.mapper.data.M3U8Mapper;
import com.mao.service.BaseService;
import com.mao.util.JsonUtil;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

/**
 * 电影、电视m3u8播放地址
 * create by mao at 2020/4/16 23:51
 */
public class M3U8Service extends BaseService {

    /**
     * 电视播放地址列表查询
     * 返回全部列表
     */
    public void live(RoutingContext ctx){
        SqlSession session = MybatisConfig.getSession();
        M3U8Mapper mapper = session.getMapper(M3U8Mapper.class);
        List<Live> lives = mapper.getLives();
        session.close();
        sendData(ctx,lives);
    }

    /**
     * 电影列表查询
     * 分选择分页查询 和 搜索查询
     */
    public void movies(RoutingContext ctx){
        MovieParam movieParam = bodyParam(ctx,MovieParam.class);
        if (null == movieParam)
            sendError(ctx,"invalid body param");
        else {
            SqlSession session = MybatisConfig.getSession();
            M3U8Mapper mapper = session.getMapper(M3U8Mapper.class);
            if (SU.isEmpty(movieParam.getName()) && SU.isEmpty(movieParam.getActor())){
                formatMovieParam(movieParam);
                Integer page = movieParam.getPage();
                transPage(movieParam);
                List<Movie> movies = mapper.getMovies(movieParam);
                Long totalPage = mapper.getMoviesTotalPage(movieParam);
                PageData<Movie> data = pageData(movies,page,totalPage,movieParam);
                session.close();
                sendData(ctx,data);
            } else {
                List<Movie> movies = mapper.searchMovies(movieParam);
                session.close();
                sendData(ctx,movies);
            }
        }
    }

    /**
     * 电影查询参数的整理
     */
    private void formatMovieParam(MovieParam movieParam){
        int type_id = 0;
        if (SU.isNotEmpty(movieParam.getType()))
            for (MovieTypeEnum typeEnum : MovieTypeEnum.values())
                if (movieParam.getType().equals(typeEnum.getName())){
                    type_id = typeEnum.getId();
                    break;
                }
        movieParam.setType_id(type_id);
        if (null == movieParam.getTime() || movieParam.getTime() < 0)
            movieParam.setTime(null);
        int place_id = 0;
        if (SU.isNotEmpty(movieParam.getPlace()))
            for (MoviePlaceEnum placeEnum : MoviePlaceEnum.values())
                if (movieParam.getPlace().equals(placeEnum.getName())){
                    place_id = placeEnum.getId();
                    break;
                }
        movieParam.setPlace_id(place_id);
    }

    /**
     * 电影详情的查询
     */
    public void movie(RoutingContext ctx){
        Long id = pathLong(ctx,"id");
        if (null == id)
            sendError(ctx,"invalid param id");
        else {
            SqlSession session = MybatisConfig.getSession();
            M3U8Mapper mapper = session.getMapper(M3U8Mapper.class);
            MovieSrc movie = mapper.getMovie(id);
            session.close();
            if (null == movie)
                sendError(ctx,"invalid param id");
            else {
                formatMovie(movie);
                sendData(ctx,movie);
            }
        }
    }

    /**
     * 电影播放地址json数据的转化
     */
    private void formatMovie(MovieSrc movie){
        if (SU.isEmpty(movie.getM3u8())){
            movie.setM3u8("no");
            movie.setUrls(new ArrayList<>(0));
        } else {
            List<MovieM3u8> urls = JsonUtil.json2listObj(movie.getM3u8(), MovieM3u8.class);
            if (null == urls){
                movie.setM3u8("no");
                movie.setUrls(new ArrayList<>(0));
            } else {
                movie.setM3u8("ok");
                movie.setUrls(urls);
            }
        }
    }

}
