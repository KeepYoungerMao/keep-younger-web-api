package com.mao;

import com.mao.entity.Server;
import com.mao.his.bd.BaiDuMap;
import com.mao.his.weather.ChinaWeather;
import com.mao.service.MainService;
import com.mao.service.auth.AuthService;
import com.mao.service.data.BookService;
import com.mao.util.PropertiesReader;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.FaviconHandler;

/**
 * 服务开启
 * @author zongx at 2020/4/7 21:38
 */
public class MainVerticle extends AbstractVerticle {

    //服务器信息
    public static final Server server = PropertiesReader.readServer("config/server.properties");
    //过滤路径
    public static final String[] FILTER_PATH = new String[]{"/auth/*","/api/*","/file/*"};

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route("/favicon.ico").handler(FaviconHandler.create("favicon.ico"));
        router.route("/file/*").subRouter(file());
        router.route().handler(MainService::filter);
        router.route("/").handler(MainService::index);
        router.route("/auth/*").subRouter(auth());
        router.route("/api/*").subRouter(api());
        router.route("/his/*").subRouter(his());
        router.errorHandler(404, MainService::notFound);
        router.errorHandler(401,MainService::permission);
        router.errorHandler(405,MainService::notAllowed);
        router.errorHandler(500,MainService::error);
        vertx.createHttpServer().requestHandler(router).listen(server.getPort());
    }

    /**
     * auth类请求
     */
    private Router auth(){
        Router authRouter = Router.router(vertx);
        authRouter.route("/token").handler(AuthService::token);
        authRouter.route("/token/flush").handler(AuthService::flush);
        return authRouter;
    }

    /**
     * his类请求
     */
    private Router his(){
        Router hisRouter = Router.router(vertx);
        hisRouter.get("/address/ip").handler(BaiDuMap::addressIp);
        hisRouter.get("/weather/city").handler(ChinaWeather::getWeather);
        return hisRouter;
    }

    /**
     * api类请求
     */
    private Router api(){
        Router apiRouter = Router.router(vertx);
        apiRouter.get("/data/book").handler(BodyHandler.create()).handler(BookService::getBooks);
        return apiRouter;
    }

    /**
     * file类请求
     */
    private Router file(){
        Router fileRouter = Router.router(vertx);
        fileRouter.route("/image/*").handler(MainService::image);
        return fileRouter;
    }

}
