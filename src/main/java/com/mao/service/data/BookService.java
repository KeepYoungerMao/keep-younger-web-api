package com.mao.service.data;

import com.mao.config.MybatisConfig;
import com.mao.entity.PageData;
import com.mao.entity.data.book.*;
import com.mao.entity.response.Response;
import com.mao.mapper.data.BookMapper;
import com.mao.util.JsonUtil;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 古籍处理
 * @author zongx at 2020/4/8 20:58
 */
public class BookService {

    public static void getBooks(RoutingContext ctx){
        BookParam bookParam = JsonUtil.json2obj(ctx.getBodyAsString(), BookParam.class);
        if (null == bookParam)
            ctx.response().end(Response.error("cannot find params body"));
        else {
            SqlSession session = MybatisConfig.getSession();
            BookMapper mapper = session.getMapper(BookMapper.class);
            if (SU.isEmpty(bookParam.getAuth()) && SU.isEmpty(bookParam.getName())){
                Integer page = bookParam.getPage();
                bookParam.setPage(page <= 1 ? 0 : (bookParam.getPage() - 1)*bookParam.getRow());
                List<Book> books = mapper.getBooks(bookParam);
                long totalPage = mapper.getBookTotalPage(bookParam);
                PageData<Book> data = new PageData<>(SU.ceil(totalPage,bookParam.getRow()),
                        bookParam.getRow(),page,bookParam,books);
                session.close();
                ctx.response().end(Response.ok(data));
            } else {
                List<Book> books = mapper.searchBooks(bookParam);
                session.close();
                ctx.response().end(Response.ok(books));
            }
        }
    }

    public static void getBook(RoutingContext ctx){
        Long id = SU.parse(ctx.pathParam("id"));
        if (null == id)
            ctx.response().end(Response.error("invalid book id"));
        else {
            SqlSession session = MybatisConfig.getSession();
            BookMapper mapper = session.getMapper(BookMapper.class);
            BookSrc book = mapper.getBookById(id);
            if (null == book){
                session.close();
                ctx.response().end(Response.error("invalid book id"));
            } else {
                List<BookChapter> chapters = mapper.getBookChapterByBookId(id);
                book.setChapters(chapters);
                session.close();
                ctx.response().end(Response.ok(book));
            }
        }
    }

    public static void getChapter(RoutingContext ctx){
        Long id = SU.parse(ctx.pathParam("id"));
        if (null == id)
            ctx.response().end(Response.error("invalid chapter id"));
        else {
            SqlSession session = MybatisConfig.getSession();
            BookMapper mapper = session.getMapper(BookMapper.class);
            BookChapterSrc chapter = mapper.getBookChapterById(id);
            session.close();
            ctx.response().end(Response.ok(chapter));
        }
    }

}
