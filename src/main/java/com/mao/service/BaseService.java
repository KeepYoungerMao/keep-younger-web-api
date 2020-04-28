package com.mao.service;

import com.mao.entity.Page;
import com.mao.entity.PageData;
import com.mao.entity.response.Response;
import com.mao.util.JsonUtil;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;

import java.util.List;

/**
 * 基本数据处理
 * @author : create by zongx at 2020/4/28 14:38
 */
public class BaseService {

    public static<T> T bodyParam(RoutingContext ctx, Class<T> clazz){
        String body = ctx.getBodyAsString();
        if (SU.isEmpty(body))
            return null;
        return JsonUtil.json2obj(body,clazz);
    }

    public static Long pathLong(RoutingContext ctx, String param){
        String pathParam = ctx.pathParam(param);
        if (SU.isEmpty(pathParam))
            return null;
        try {
            return Long.parseLong(pathParam);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void sendError(RoutingContext ctx, String msg){
        ctx.response().end(Response.error(msg));
    }

    public static<T> void sendData(RoutingContext ctx, T data){
        ctx.response().end(Response.ok(data));
    }

    public static void transPage(Page page){
        page.setPage(page.getPage() <= 1 ? 0 : (page.getPage() - 1) * page.getRow());
    }

    public static<T,V extends Page> PageData<T> pageData(List<T> data, int curPage, long totalPage, V page){
        return new PageData<>(SU.ceil(totalPage, page.getRow()), page.getRow(), curPage, page, data);
    }

}
