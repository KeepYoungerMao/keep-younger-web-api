package com.mao.service.data;

import com.mao.config.MybatisConfig;
import com.mao.entity.PageData;
import com.mao.entity.data.bjx.Bjx;
import com.mao.entity.data.bjx.BjxParam;
import com.mao.entity.data.bjx.BjxSrc;
import com.mao.entity.response.Response;
import com.mao.mapper.data.BjxMapper;
import com.mao.util.JsonUtil;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 百家姓处理
 * create by mao at 2020/4/15 22:14
 */
public class BjxService {

    public static void getBjx(RoutingContext ctx){
        BjxParam bjxParam = JsonUtil.json2obj(ctx.getBodyAsString(), BjxParam.class);
        if (null == bjxParam)
            ctx.response().end(Response.error("cannot find param body"));
        else {
            SqlSession session = MybatisConfig.getSession();
            BjxMapper mapper = session.getMapper(BjxMapper.class);
            if (SU.isEmpty(bjxParam.getName())){
                Integer page = bjxParam.getPage();
                bjxParam.setPage(page <= 1 ? 0 : (bjxParam.getPage() - 1)*bjxParam.getRow());
                List<Bjx> bjx = mapper.getBjx(bjxParam);
                Long totalPage = mapper.getBjxTotalPage(bjxParam);
                PageData<Bjx> data = new PageData<>(SU.ceil(totalPage,bjxParam.getRow()),
                        bjxParam.getRow(),page,bjxParam,bjx);
                session.close();
                ctx.response().end(Response.ok(data));
            } else {
                List<Bjx> list = mapper.searchBjx(bjxParam);
                session.close();
                ctx.response().end(Response.ok(list));
            }
        }
    }

    public static void getBjxSrc(RoutingContext ctx){
        Long id = SU.parse(ctx.pathParam("id"));
        if (null != id) {
            SqlSession session = MybatisConfig.getSession();
            BjxMapper mapper = session.getMapper(BjxMapper.class);
            BjxSrc src = mapper.getBjxSrc(id);
            session.close();
            ctx.response().end(Response.ok(src));
        } else {
            ctx.response().end(Response.error("invalid param id"));
        }
    }

}
