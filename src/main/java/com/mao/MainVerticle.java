package com.mao;

import com.mao.entity.Server;
import com.mao.his.bd.BaiDuMap;
import com.mao.his.sudo.SudoKuService;
import com.mao.his.weather.ChinaWeather;
import com.mao.service.MainService;
import com.mao.service.auth.AuthService;
import com.mao.service.data.*;
import com.mao.util.HttpUtil;
import com.mao.util.PropertiesReader;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.FaviconHandler;

/**
 * 服务开启
 * @author zongx at 2020/4/7 21:38
 */
public class MainVerticle extends AbstractVerticle {

    //服务器信息
    public static final Server server = PropertiesReader.readServer("/config/server.properties");
    //过滤路径，此处只实现以何路径开头的拦截
    public static final String[] FILTER_PATH = new String[]{"/his","/api","/file"};
    //图片数据储存位置前缀（至image文件夹前）
    public static final String IMAGE_FILE_LOCAL_PATH_PRE = "D:";
    //web客户端
    public static final WebClient webClient = HttpUtil.getWebClient(Vertx.vertx());

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route("/favicon.ico").handler(FaviconHandler.create("favicon.ico"));
        router.route().handler(MainService::filter);
        router.route("/").handler(MainService::index);
        router.route("/file/*").subRouter(file());
        if (server.isNeedAuthorize())
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
        authRouter.route("/authorize").handler(AuthService::authorize);
        authRouter.route("/refresh").handler(AuthService::refresh);
        return authRouter;
    }

    /**
     * his类请求
     */
    private Router his(){
        Router hisRouter = Router.router(vertx);
        hisRouter.get("/address/ip").handler(BaiDuMap::addressIp);
        hisRouter.get("/weather/city").handler(ChinaWeather::getWeather);
        hisRouter.post("/sudoKu").handler(BodyHandler.create()).handler(SudoKuService::sudoKu);
        return hisRouter;
    }

    /**
     * api类请求
     */
    private Router api(){
        Router apiRouter = Router.router(vertx);
        apiRouter.get("/data/book").handler(BodyHandler.create()).handler(BookService::getBooks);
        apiRouter.get("/data/book/:id").handler(BookService::getBook);
        apiRouter.get("/data/book/chapter/:id").handler(BookService::getChapter);
        apiRouter.get("/data/bjx").handler(BodyHandler.create()).handler(BjxService::getBjx);
        apiRouter.get("/data/bjx/:id").handler(BjxService::getBjxSrc);
        apiRouter.get("/data/buddhist").handler(BodyHandler.create()).handler(BuddhistService::getBuddhists);
        apiRouter.get("/data/buddhist/:id").handler(BuddhistService::getBuddhist);
        apiRouter.get("/data/buddhist/chapter/:id").handler(BuddhistService::getChapter);
        apiRouter.get("/data/pic/class").handler(PicService::getPicClass);
        apiRouter.get("/data/pic").handler(BodyHandler.create()).handler(PicService::getPics);
        apiRouter.get("/data/pic/image/:id").handler(PicService::getPic);
        apiRouter.get("/data/m3u8/live").handler(M3U8Service::live);
        apiRouter.get("/data/m3u8/movie").handler(BodyHandler.create()).handler(M3U8Service::movies);
        apiRouter.get("/data/m3u8/movie/:id").handler(M3U8Service::movie);
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
