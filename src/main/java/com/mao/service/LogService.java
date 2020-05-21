package com.mao.service;

import com.mao.entity.response.Response;
import io.vertx.ext.web.RoutingContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 日志操作
 * @author : create by zongx at 2020/4/29 14:15
 */
public class LogService {

    private final ExecutorService service = Executors.newFixedThreadPool(4);

    public void log(RoutingContext ctx) {
        if (ctx.response().bytesWritten() > 0){
            String path = ctx.request().path();
            int code = ctx.response().getStatusCode();
            saveLog(path,code);
        } else {
            ctx.response().setStatusCode(400);
            ctx.response().write(Response.notFound());
        }
        ctx.response().end();
    }

    public void saveLog(String path, int status) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("save log");
            return 1;
        },service);
        //future.thenAccept(System.out::println);
    }

}
