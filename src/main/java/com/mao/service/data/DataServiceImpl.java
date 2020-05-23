package com.mao.service.data;

import com.mao.config.MybatisConfig;
import com.mao.entity.PageData;
import com.mao.entity.data.bjx.Bjx;
import com.mao.entity.data.bjx.BjxParam;
import com.mao.entity.data.bjx.BjxSrc;
import com.mao.entity.data.book.*;
import com.mao.entity.data.buddhist.*;
import com.mao.entity.data.m3u8.*;
import com.mao.entity.data.pic.Pic;
import com.mao.entity.data.pic.PicClass;
import com.mao.entity.data.pic.PicParam;
import com.mao.entity.data.pic.PicSrc;
import com.mao.mapper.data.*;
import com.mao.service.BaseService;
import com.mao.util.JsonUtil;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据查询处理
 * @author : create by zongx at 2020/5/19 15:37
 */
public class DataServiceImpl extends BaseService implements DataService {

    /**
     * 古籍图片地址前缀
     */
    private static final String BOOK_IMAGE_PRE = "/file/image/book/";

    /**
     * 佛经图片地址前缀
     */
    private static final String BUDDHIST_IMAGE_PRE = "/file/image/buddhist/";

    /**
     * 小图片地址前缀
     */
    private static final String PIC_S_IMAGE_PRE = "/file/image/pic/";

    /**
     * 大图片地址前缀
     */
    private static final String PIC_IMAGE_PRE = "/file/image/pic_big/";

    /**
     * 查询类型
     */
    private enum SearchType{
        BJX,                //百家姓
        BOOK,               //古籍
        BOOK_CHAPTER,       //估计章节
        BUDDHIST,           //佛经
        BUDDHIST_CHAPTER,   //佛经章节
        LIVE,               //直播
        MOVIE,              //电影
        PIC,                //图片
        PIC_CLASS,          //图片分类
        ERROR               //错误类型
    }

    /**
     * 将参数转换为查询类型Enum
     * 转换失败返回ERROR类型Enum
     * @param item 查询类型
     * @return 查询类型Enum
     */
    private SearchType searchType(String item){
        try {
            return SearchType.valueOf(item.toUpperCase());
        } catch (Exception e) {
            return SearchType.ERROR;
        }
    }

    /**
     * 查询各类数据的列表
     * 查询各类型数据的列表的查询参数都是通过body传参
     * POST类型
     * 统一数据类型返回
     * 查询之前获取SqlSession，查询数据完成后关闭session
     * 写入数据后，交由下一个Router进行处理（在LogService中进行end）
     */
    public void searchList(RoutingContext ctx){
        String item = ctx.pathParam("item");
        SqlSession session = MybatisConfig.getSession();
        String data;
        switch (searchType(item)){
            case BJX:
                BjxParam bjxParam = bodyParam(ctx,BjxParam.class);
                BjxMapper bjxMapper = session.getMapper(BjxMapper.class);
                data = null == bjxParam ? bodyParamError() : searchBjxList(bjxParam,bjxMapper);
                break;
            case BOOK:
                BookParam bookParam = bodyParam(ctx,BookParam.class);
                BookMapper bookMapper = session.getMapper(BookMapper.class);
                data = null == bookParam ? bodyParamError() : searchBookList(bookParam,bookMapper);
                break;
            case BUDDHIST:
                BuddhistParam buddhistParam = bodyParam(ctx,BuddhistParam.class);
                BuddhistMapper buddhistMapper = session.getMapper(BuddhistMapper.class);
                data = null == buddhistParam ? bodyParamError() : searchBuddhistList(buddhistParam,buddhistMapper);
                break;
            case LIVE:
                M3U8Mapper m3U8Mapper = session.getMapper(M3U8Mapper.class);
                data = searchLiveList(m3U8Mapper);
                break;
            case MOVIE:
                MovieParam movieParam = bodyParam(ctx,MovieParam.class);
                M3U8Mapper m3U8Mapper2 = session.getMapper(M3U8Mapper.class);
                data = null == movieParam ? bodyParamError() : searchMovieList(movieParam,m3U8Mapper2);
                break;
            case PIC_CLASS:
                PicMapper picMapper = session.getMapper(PicMapper.class);
                data = searchPicClassList(picMapper);
                break;
            case PIC:
                PicParam picParam = bodyParam(ctx,PicParam.class);
                PicMapper picMapper2 = session.getMapper(PicMapper.class);
                data = null == picParam ? bodyParamError() : searchPicList(picParam,picMapper2);
                break;
            case ERROR:
            default:
                data = searchError(item);
                break;
        }
        session.close();
        sendData(ctx,data);
    }

    /**
     * 查询图片列表数据
     * @param picParam 查询参数
     * @param mapper mapper接口
     * @return 图片列表
     */
    private String searchPicList(PicParam picParam, PicMapper mapper){
        if (SU.isEmpty(picParam.getName())){
            if (SU.isNotZs(picParam.getPid()) || SU.isNotZs(picParam.getSid()))
                return paramError("param is necessary");
            else {
                Integer page = picParam.getPage();
                transPage(picParam);
                List<Pic> pics = mapper.getPics(picParam);
                //图片路径的补全
                pics.forEach(pic -> pic.setS_image(PIC_S_IMAGE_PRE + pic.getPrl() + "/" + pic.getSrl() + "/" + pic.getS_image()));
                Long totalPage = mapper.getPicsTotalPage(picParam);
                PageData<Pic> data = pageData(pics,page,totalPage,picParam);
                return ok(data);
            }
        } else {
            List<Pic> pics = mapper.searchPics(picParam);
            return ok(pics);
        }
    }

    /**
     * 查询图片分类列表
     * @param mapper mapper接口
     * @return 图片分类列表
     */
    private String searchPicClassList(PicMapper mapper){
        List<PicClass> picClass = mapper.getPicClass();
        return ok(picClass);
    }

    /**
     * 查询电影列表数据
     * @param movieParam 查询参数
     * @param mapper mapper接口
     * @return 电影列表
     */
    private String searchMovieList(MovieParam movieParam, M3U8Mapper mapper){
        if (SU.isEmpty(movieParam.getName()) && SU.isEmpty(movieParam.getActor())){
            formatMovieParam(movieParam);
            Integer page = movieParam.getPage();
            transPage(movieParam);
            List<Movie> movies = mapper.getMovies(movieParam);
            Long totalPage = mapper.getMoviesTotalPage(movieParam);
            PageData<Movie> data = pageData(movies,page,totalPage,movieParam);
            return ok(data);
        } else {
            List<Movie> movies = mapper.searchMovies(movieParam);
            return ok(movies);
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
     * 查询全部直播列表
     * @param mapper mapper接口
     * @return 直播列表
     */
    private String searchLiveList(M3U8Mapper mapper){
        List<Live> lives = mapper.getLives();
        return ok(lives);
    }

    /**
     * 查询佛经列表数据
     * @param buddhistParam 查询参数
     * @param mapper mapper接口
     * @return 佛经列表
     */
    private String searchBuddhistList(BuddhistParam buddhistParam, BuddhistMapper mapper){
        if (SU.isEmpty(buddhistParam.getName()) && SU.isEmpty(buddhistParam.getAuth())){
            Integer page = buddhistParam.getPage();
            transPage(buddhistParam);
            List<Buddhist> buddhists = mapper.getBuddhists(buddhistParam);
            //补全佛经图片路径
            buddhists.forEach(buddhist -> buddhist.setImage(BUDDHIST_IMAGE_PRE + buddhist.getImage()));
            Long totalPage = mapper.getBuddhistTotalPage(buddhistParam);
            PageData<Buddhist> data = pageData(buddhists,page,totalPage,buddhistParam);
            return ok(data);
        } else {
            List<Buddhist> buddhists = mapper.searchBuddhists(buddhistParam);
            return ok(buddhists);
        }
    }

    /**
     * 查询古籍列表数据
     * @param bookParam 查询参数
     * @param mapper mapper接口
     * @return 古籍列表
     */
    private String searchBookList(BookParam bookParam, BookMapper mapper){
        if (SU.isEmpty(bookParam.getAuth()) && SU.isEmpty(bookParam.getName())){
            Integer page = bookParam.getPage();
            transPage(bookParam);
            List<Book> books = mapper.getBooks(bookParam);
            //图片路径的补全
            books.forEach(book -> book.setS_image(BOOK_IMAGE_PRE + book.getS_image()));
            long totalPage = mapper.getBookTotalPage(bookParam);
            PageData<Book> data = pageData(books,page,totalPage,bookParam);
            return ok(data);
        } else {
            List<Book> books = mapper.searchBooks(bookParam);
            return ok(books);
        }
    }

    /**
     * 查询百家姓列表数据
     * @param bjxParam 查询参数
     * @param mapper mapper接口
     * @return 百家姓列表数据
     */
    private String searchBjxList(BjxParam bjxParam, BjxMapper mapper){
        if (SU.isEmpty(bjxParam.getName())){
            Integer page = bjxParam.getPage();
            transPage(bjxParam);
            List<Bjx> bjx = mapper.getBjx(bjxParam);
            Long totalPage = mapper.getBjxTotalPage(bjxParam);
            PageData<Bjx> data = pageData(bjx,page,totalPage,bjxParam);
            return ok(data);
        } else {
            List<Bjx> list = mapper.searchBjx(bjxParam);
            return ok(list);
        }
    }

    /**
     * 查询各类数据详情
     * 统一查询各类数据详情
     * GET类型
     * 统一返回数据类型
     */
    public void searchItem(RoutingContext ctx){
        String item = ctx.pathParam("item");
        Long id = pathLong(ctx,"id");
        String data;
        if (null == id)
            data = paramError("id");
        else {
            SqlSession session = MybatisConfig.getSession();
            switch (searchType(item)){
                case BJX:
                    BjxMapper bjxMapper = session.getMapper(BjxMapper.class);
                    data = searchBjxItem(id,bjxMapper);
                    break;
                case BOOK:
                    BookMapper bookMapper = session.getMapper(BookMapper.class);
                    data = searchBookItem(id,bookMapper);
                    break;
                case BOOK_CHAPTER:
                    BookMapper bookMapper2 = session.getMapper(BookMapper.class);
                    data = searchBookChapterItem(id,bookMapper2);
                    break;
                case BUDDHIST:
                    BuddhistMapper buddhistMapper = session.getMapper(BuddhistMapper.class);
                    data = searchBuddhistItem(id,buddhistMapper);
                    break;
                case BUDDHIST_CHAPTER:
                    BuddhistMapper buddhistMapper2 = session.getMapper(BuddhistMapper.class);
                    data = searchBuddhistChapterItem(id,buddhistMapper2);
                    break;
                case MOVIE:
                    M3U8Mapper m3U8Mapper = session.getMapper(M3U8Mapper.class);
                    data = searchMovieItem(id, m3U8Mapper);
                    break;
                case PIC:
                    PicMapper picMapper = session.getMapper(PicMapper.class);
                    data = searchPicItem(id,picMapper);
                    break;
                case ERROR:
                default:
                    data = searchError(item);
                    break;
            }
            session.close();
        }
        sendData(ctx,data);
    }

    /**
     * 根据id查询图片详情
     * @param id 图片id
     * @param mapper mapper接口
     * @return 图片详情
     */
    private String searchPicItem(Long id, PicMapper mapper){
        PicSrc src = mapper.getPicSrc(id);
        if (null != src && SU.isNotEmpty(src.getId())){
            src.setS_image(PIC_S_IMAGE_PRE + src.getPrl() + "/" + src.getSrl() + "/" + src.getS_image());
            src.setImage(PIC_IMAGE_PRE + src.getPrl() + "/" + src.getSrl() + "/" + src.getImage());
        }
        return ok(src);
    }

    /**
     * 根据id查询电影详情
     * @param id 电影id
     * @param mapper mapper接口
     * @return 电影详情
     */
    private String searchMovieItem(Long id, M3U8Mapper mapper){
        MovieSrc movie = mapper.getMovie(id);
        if (null != movie)
            formatMovie(movie);
        return ok(movie);
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

    /**
     * 根据id查询佛经章节详情
     * @param id 佛经章节id
     * @param mapper mapper接口
     * @return 佛经章节详情
     */
    private String searchBuddhistChapterItem(Long id, BuddhistMapper mapper){
        BuddhistChapterSrc chapter = mapper.getChapter(id);
        return ok(chapter);
    }

    /**
     * 根据id查询佛经详情
     * @param id 佛经id
     * @param mapper mapper接口
     * @return 佛经详情
     */
    private String searchBuddhistItem(Long id, BuddhistMapper mapper){
        BuddhistSrc buddhist = mapper.getBuddhist(id);
        if (null != buddhist && SU.isNotEmpty(buddhist.getId())){
            buddhist.setImage(BUDDHIST_IMAGE_PRE + buddhist.getImage());
            List<BuddhistChapter> chapters = mapper.getChapters(id);
            buddhist.setChapters(chapters);
        }
        return ok(buddhist);
    }

    /**
     * 根据id查询古籍章节详情
     * @param id 古籍章节id
     * @param mapper mapper接口
     * @return 古籍章节详情
     */
    private String searchBookChapterItem(Long id, BookMapper mapper){
        BookChapterSrc chapter = mapper.getBookChapterById(id);
        return ok(chapter);
    }

    /**
     * 根据id查询古籍详情
     * @param id 古籍id
     * @param mapper mapper接口
     * @return 古籍详情
     */
    private String searchBookItem(Long id, BookMapper mapper){
        BookSrc book = mapper.getBookById(id);
        if (null != book && SU.isNotEmpty(book.getId())){
            //补全图片路径
            book.setS_image(BOOK_IMAGE_PRE + book.getS_image());
            book.setImage(BOOK_IMAGE_PRE + book.getImage());
            //查询该古籍的所有章节列表
            List<BookChapter> chapters = mapper.getBookChapterByBookId(id);
            book.setChapters(chapters);
        }
        return ok(book);
    }

    /**
     * 根据id查询百家姓详情
     * @param id 百家姓id
     * @param mapper mapper接口
     * @return 百家姓详情
     */
    private String searchBjxItem(Long id, BjxMapper mapper){
        BjxSrc src = mapper.getBjxSrc(id);
        return ok(src);
    }

}
