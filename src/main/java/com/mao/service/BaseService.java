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

    /**
     * 获取body内容参数
     * @param ctx 上下文
     * @param clazz 参数实体类型
     * @param <T> 参数实体泛型
     * @return 参数值
     */
    public static<T> T bodyParam(RoutingContext ctx, Class<T> clazz){
        String body = ctx.getBodyAsString();
        if (SU.isEmpty(body))
            return null;
        return JsonUtil.json2obj(body,clazz);
    }

    /**
     * 获取body内容参数
     * 获取集合类型的body参数
     * @param ctx 上下文
     * @param clazz 参数实体类型
     * @param <T> 参数实体泛型
     * @return 参数值
     */
    public static<T> List<T> bodyListParam(RoutingContext ctx, Class<T> clazz){
        String body = ctx.getBodyAsString();
        if (SU.isEmpty(body))
            return null;
        return JsonUtil.json2listObj(body,clazz);
    }

    /**
     * 获取地址拼接参数
     * 获取long型参数
     * @param ctx 上下文
     * @param param 参数名
     * @return 参数值
     */
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

    /**
     * 获取链接后参数
     * @param ctx 上下文
     * @param param 参数名
     * @return 参数值
     */
    public static String paramString(RoutingContext ctx, String param){
        return ctx.request().getParam(param);
    }

    /**
     * 发送错误数据
     * 将错误信息包装后发送
     * @param ctx 上下文
     * @param msg 错误提示
     */
    public static void sendError(RoutingContext ctx, String msg){
        send(ctx,Response.error(msg),500);
    }

    /**
     * 发送数据
     * 发送没有封装成ResponseData类型数据的数据
     * 封装后再发送
     * @param ctx 上下文
     * @param data 数据
     * @param <T> 数据泛型
     */
    public static<T> void sendData(RoutingContext ctx, T data){
        send(ctx,Response.ok(data),200);
    }

    /**
     * 发送数据
     * 发送已经封装好的ResponseData数据
     * @param ctx 上下文
     * @param data 数据字符串
     */
    public static void sendData(RoutingContext ctx, String data){
        send(ctx,data,200);
    }

    /**
     * 发送数据
     * @param ctx 上下文
     * @param msg 发送的数据字符串
     * @param status 发送的状态
     */
    private static void send(RoutingContext ctx, String msg, int status){
        ctx.response().setStatusCode(status);
        ctx.response().putHeader("Content-Length",String.valueOf(msg.getBytes().length));
        ctx.response().write(msg);
        ctx.next();
    }

    /**
     * 页码转换
     * 前端：从1开始的页码
     * 后台数据查询：查询数起始位置
     * @param page 前端页码数
     */
    public static void transPage(Page page){
        page.setPage(page.getPage() <= 1 ? 0 : (page.getPage() - 1) * page.getRow());
    }

    /**
     * 封装分页数据
     * 需要分页的数据统一以PageData类型数据进行封装
     * @param data 实际数据
     * @param curPage 当前页
     * @param totalPage 总页数
     * @param page 分页查询参数
     * @param <T> 实际数据泛型
     * @param <V> 分页查询参数 继承于分页参数Page
     * @return 分页数据
     */
    public static<T,V extends Page> PageData<T> pageData(List<T> data, int curPage, long totalPage, V page){
        return new PageData<>(SU.ceil(totalPage, page.getRow()), page.getRow(), curPage, page, data);
    }

}
