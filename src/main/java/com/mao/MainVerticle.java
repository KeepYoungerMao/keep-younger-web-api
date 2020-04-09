package com.mao;

import com.mao.entity.Server;
import com.mao.his.bd.BaiDuMap;
import com.mao.his.weather.ChinaWeather;
import com.mao.service.MainService;
import com.mao.service.data.BookService;
import com.mao.util.PropertiesReader;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

/**
 * 服务开启
 * @author zongx at 2020/4/7 21:38
 */
public class MainVerticle extends AbstractVerticle {

    public static final Server server = PropertiesReader.readServer("config/server.properties");

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().order(-1).handler(MainService::filter);
        router.route("/").handler(MainService::index);
        router.get("/api/data/book").handler(BodyHandler.create()).handler(BookService::getBooks);
        router.get("/his/address/ip").handler(BaiDuMap::addressIp);
        router.get("/his/weather/city").handler(ChinaWeather::getWeather);
        router.errorHandler(404, MainService::notFound);
        router.errorHandler(401,MainService::permission);
        router.errorHandler(405,MainService::notAllowed);
        router.errorHandler(500,MainService::error);
        vertx.createHttpServer().requestHandler(router).listen(8080);
    }

}
