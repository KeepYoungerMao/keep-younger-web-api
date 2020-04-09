package com.mao.service.data;

import com.mao.config.MybatisConfig;
import com.mao.entity.PageData;
import com.mao.entity.data.Book;
import com.mao.entity.data.BookParam;
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
            ctx.response().end(Response.error("cannot find params body",ctx.request().path()));
        else {
            System.out.println(bookParam);
            SqlSession session = MybatisConfig.getSession();
            BookMapper mapper = session.getMapper(BookMapper.class);
            if (SU.isEmpty(bookParam.getAuth()) && SU.isEmpty(bookParam.getName())){
                List<Book> books = mapper.getBooks(bookParam);
                long totalPage = mapper.getBookTotalPage(bookParam);
                PageData<Book> data = new PageData<>();
                data.setTotal(totalPage);
                data.setRows(bookParam.getRow());
                data.setPage(bookParam.getPage());
                data.setFilter(bookParam);
                data.setData(books);
                session.close();
                ctx.response().end(Response.ok(data));
            } else {
                List<Book> books = mapper.searchBooks(bookParam);
                session.close();
                ctx.response().end(Response.ok(books));
            }
        }
    }

}
