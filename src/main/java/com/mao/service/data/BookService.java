package com.mao.service.data;

import com.mao.config.MybatisConfig;
import com.mao.entity.PageData;
import com.mao.entity.data.book.*;
import com.mao.mapper.data.BookMapper;
import com.mao.service.BaseService;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 古籍处理
 * @author zongx at 2020/4/8 20:58
 */
public class BookService extends BaseService {

    /**
     * 古籍图片地址前缀
     */
    private static final String IMAGE_PRE = "/file/image/book/";

    /**
     * 古籍列表查询
     * 分选择分页查询 和 搜索查询
     */
    public void getBooks(RoutingContext ctx){
        BookParam bookParam = bodyParam(ctx,BookParam.class);
        if (null == bookParam)
            sendError(ctx,"cannot find params body");
        else {
            SqlSession session = MybatisConfig.getSession();
            BookMapper mapper = session.getMapper(BookMapper.class);
            if (SU.isEmpty(bookParam.getAuth()) && SU.isEmpty(bookParam.getName())){
                Integer page = bookParam.getPage();
                transPage(bookParam);
                List<Book> books = mapper.getBooks(bookParam);
                formatBookImage(books);
                long totalPage = mapper.getBookTotalPage(bookParam);
                PageData<Book> data = pageData(books,page,totalPage,bookParam);
                session.close();
                sendData(ctx,data);
            } else {
                List<Book> books = mapper.searchBooks(bookParam);
                session.close();
                sendData(ctx,books);
            }
        }
    }

    /**
     * 图片路径的补全
     */
    private void formatBookImage(List<Book> books){
        books.forEach(book -> book.setS_image(IMAGE_PRE + book.getS_image()));
    }

    /**
     * 图片路径的补全
     */
    private void formatBookImage(BookSrc src){
        if (null != src){
            src.setS_image(IMAGE_PRE + src.getS_image());
            src.setImage(IMAGE_PRE + src.getImage());
        }
    }

    /**
     * 古籍详情的查询
     * 附带古籍章节全部列表
     */
    public void getBook(RoutingContext ctx){
        Long id = pathLong(ctx,"id");
        if (null == id)
            sendError(ctx,"invalid book id");
        else {
            SqlSession session = MybatisConfig.getSession();
            BookMapper mapper = session.getMapper(BookMapper.class);
            BookSrc book = mapper.getBookById(id);
            if (null == book){
                session.close();
                sendError(ctx,"invalid book id");
            } else {
                formatBookImage(book);
                List<BookChapter> chapters = mapper.getBookChapterByBookId(id);
                book.setChapters(chapters);
                session.close();
                sendData(ctx,book);
            }
        }
    }

    /**
     * 古籍章节详情的查询
     */
    public void getChapter(RoutingContext ctx){
        Long id = pathLong(ctx,"id");
        if (null == id)
            sendError(ctx,"invalid chapter id");
        else {
            SqlSession session = MybatisConfig.getSession();
            BookMapper mapper = session.getMapper(BookMapper.class);
            BookChapterSrc chapter = mapper.getBookChapterById(id);
            session.close();
            sendData(ctx,chapter);
        }
    }

}
