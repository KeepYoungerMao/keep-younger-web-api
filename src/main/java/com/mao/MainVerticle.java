package com.mao;

import com.mao.entity.Server;
import com.mao.service.MainService;
import com.mao.util.PropertiesReader;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

/**
 * 服务开启
 * @author zongx at 2020/4/7 21:38
 */
public class MainVerticle extends AbstractVerticle {

    public static final Server server = PropertiesReader.readServer("/config/server.properties");

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().order(-1).handler(MainService::filter);
        router.route("/").handler(MainService::index);
        router.errorHandler(404, MainService::notFound);
        router.errorHandler(401,MainService::permission);
        router.errorHandler(500,MainService::error);
        vertx.createHttpServer().requestHandler(router).listen(8080);
    }

}
