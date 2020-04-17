package com.mao.service.data;

import com.mao.config.MybatisConfig;
import com.mao.entity.PageData;
import com.mao.entity.data.buddhist.*;
import com.mao.entity.response.Response;
import com.mao.mapper.data.BuddhistMapper;
import com.mao.util.JsonUtil;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 佛经
 * create by mao at 2020/4/15 22:23
 */
public class BuddhistService {

    public static void getBuddhists(RoutingContext ctx){
        BuddhistParam buddhistParam = JsonUtil.json2obj(ctx.getBodyAsString(), BuddhistParam.class);
        if (null == buddhistParam)
            ctx.response().end(Response.error("cannot find body param"));
        else {
            SqlSession session = MybatisConfig.getSession();
            BuddhistMapper mapper = session.getMapper(BuddhistMapper.class);
            if (SU.isEmpty(buddhistParam.getName()) && SU.isEmpty(buddhistParam.getAuth())){
                Integer page = buddhistParam.getPage();
                buddhistParam.setPage(page <= 1 ? 0 : (buddhistParam.getPage() - 1)*buddhistParam.getRow());
                List<Buddhist> buddhists = mapper.getBuddhists(buddhistParam);
                Long totalPage = mapper.getBuddhistTotalPage(buddhistParam);
                PageData<Buddhist> data = new PageData<>(SU.ceil(totalPage,buddhistParam.getRow()),
                        buddhistParam.getRow(),page,buddhistParam,buddhists);
                session.close();
                ctx.response().end(Response.ok(data));
            } else {
                List<Buddhist> buddhists = mapper.searchBuddhists(buddhistParam);
                session.close();
                ctx.response().end(Response.ok(buddhists));
            }
        }
    }

    public static void getBuddhist(RoutingContext ctx){
        Long id = SU.parse(ctx.pathParam("id"));
        if (null == id)
            ctx.response().end(Response.error("invalid buddhist id"));
        else {
            SqlSession session = MybatisConfig.getSession();
            BuddhistMapper mapper = session.getMapper(BuddhistMapper.class);
            BuddhistSrc buddhist = mapper.getBuddhist(id);
            if (null == buddhist){
                session.close();
                ctx.response().end(Response.error("invalid buddhist id"));
            } else {
                List<BuddhistChapter> chapters = mapper.getChapters(id);
                buddhist.setChapters(chapters);
                session.close();
                ctx.response().end(Response.ok(buddhist));
            }
        }
    }

    public static void getChapter(RoutingContext ctx){
        Long id = SU.parse(ctx.pathParam("id"));
        if (null == id)
            ctx.response().end(Response.error("invalid chapter id"));
        else {
            SqlSession session = MybatisConfig.getSession();
            BuddhistMapper mapper = session.getMapper(BuddhistMapper.class);
            BuddhistChapterSrc chapter = mapper.getChapter(id);
            session.close();
            ctx.response().end(Response.ok(chapter));
        }
    }

}