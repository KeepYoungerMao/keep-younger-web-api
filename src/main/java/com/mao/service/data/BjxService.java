package com.mao.service.data;

import com.mao.config.MybatisConfig;
import com.mao.entity.PageData;
import com.mao.entity.data.bjx.Bjx;
import com.mao.entity.data.bjx.BjxParam;
import com.mao.entity.data.bjx.BjxSrc;
import com.mao.mapper.data.BjxMapper;
import com.mao.service.BaseService;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 百家姓处理
 * create by mao at 2020/4/15 22:14
 */
public class BjxService extends BaseService {

    public void getBjx(RoutingContext ctx){
        BjxParam bjxParam = bodyParam(ctx,BjxParam.class);
        if (null == bjxParam)
            sendError(ctx,"cannot find param body");
        else {
            SqlSession session = MybatisConfig.getSession();
            BjxMapper mapper = session.getMapper(BjxMapper.class);
            if (SU.isEmpty(bjxParam.getName())){
                Integer page = bjxParam.getPage();
                transPage(bjxParam);
                List<Bjx> bjx = mapper.getBjx(bjxParam);
                Long totalPage = mapper.getBjxTotalPage(bjxParam);
                PageData<Bjx> data = pageData(bjx,page,totalPage,bjxParam);
                session.close();
                sendData(ctx,data);
            } else {
                List<Bjx> list = mapper.searchBjx(bjxParam);
                session.close();
                sendData(ctx,list);
            }
        }
    }

    public void getBjxSrc(RoutingContext ctx){
        Long id = pathLong(ctx,"id");
        if (null != id) {
            SqlSession session = MybatisConfig.getSession();
            BjxMapper mapper = session.getMapper(BjxMapper.class);
            BjxSrc src = mapper.getBjxSrc(id);
            session.close();
            sendData(ctx,src);
        } else {
            sendError(ctx,"invalid param id");
        }
    }

}
