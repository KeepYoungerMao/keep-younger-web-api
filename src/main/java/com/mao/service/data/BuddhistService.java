package com.mao.service.data;

import com.mao.config.MybatisConfig;
import com.mao.entity.PageData;
import com.mao.entity.data.buddhist.*;
import com.mao.mapper.data.BuddhistMapper;
import com.mao.service.BaseService;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 佛经
 * create by mao at 2020/4/15 22:23
 */
public class BuddhistService extends BaseService {

    /**
     * 佛经图片地址前缀
     */
    private static final String IMAGE_PRE = "/file/image/buddhist/";

    /**
     * 佛经列表的查询
     * 分选择分页查询 和 搜索查询
     */
    public static void getBuddhists(RoutingContext ctx){
        BuddhistParam buddhistParam = bodyParam(ctx,BuddhistParam.class);
        if (null == buddhistParam)
            sendError(ctx,"cannot find body param");
        else {
            SqlSession session = MybatisConfig.getSession();
            BuddhistMapper mapper = session.getMapper(BuddhistMapper.class);
            if (SU.isEmpty(buddhistParam.getName()) && SU.isEmpty(buddhistParam.getAuth())){
                Integer page = buddhistParam.getPage();
                transPage(buddhistParam);
                List<Buddhist> buddhists = mapper.getBuddhists(buddhistParam);
                formatBuddhistImage(buddhists);
                Long totalPage = mapper.getBuddhistTotalPage(buddhistParam);
                PageData<Buddhist> data = pageData(buddhists,page,totalPage,buddhistParam);
                session.close();
                sendData(ctx,data);
            } else {
                List<Buddhist> buddhists = mapper.searchBuddhists(buddhistParam);
                session.close();
                sendData(ctx,buddhists);
            }
        }
    }

    /**
     * 佛经图片路径的补全
     */
    private static void formatBuddhistImage(List<Buddhist> buddhists){
        buddhists.forEach(buddhist -> buddhist.setImage(IMAGE_PRE + buddhist.getImage()));
    }

    /**
     * 佛经图片路径的补全
     */
    private static void formatBuddhistImage(BuddhistSrc buddhist){
        buddhist.setImage(IMAGE_PRE + buddhist.getImage());
    }

    /**
     * 佛经详情的查询
     * 附带佛经所有章节列表
     */
    public static void getBuddhist(RoutingContext ctx){
        Long id = pathLong(ctx,"id");
        if (null == id)
            sendError(ctx,"invalid buddhist id");
        else {
            SqlSession session = MybatisConfig.getSession();
            BuddhistMapper mapper = session.getMapper(BuddhistMapper.class);
            BuddhistSrc buddhist = mapper.getBuddhist(id);
            if (null == buddhist){
                session.close();
                sendError(ctx,"invalid buddhist id");
            } else {
                formatBuddhistImage(buddhist);
                List<BuddhistChapter> chapters = mapper.getChapters(id);
                buddhist.setChapters(chapters);
                session.close();
                sendData(ctx,buddhist);
            }
        }
    }

    /**
     * 佛经章节详情的查询
     */
    public static void getChapter(RoutingContext ctx){
        Long id = pathLong(ctx,"id");
        if (null == id)
            sendError(ctx,"invalid chapter id");
        else {
            SqlSession session = MybatisConfig.getSession();
            BuddhistMapper mapper = session.getMapper(BuddhistMapper.class);
            BuddhistChapterSrc chapter = mapper.getChapter(id);
            session.close();
            sendData(ctx,chapter);
        }
    }

}
