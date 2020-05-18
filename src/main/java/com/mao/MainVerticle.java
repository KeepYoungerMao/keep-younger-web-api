package com.mao;

import com.mao.entity.Server;
import com.mao.his.bd.BaiDuMap;
import com.mao.his.sudo.SudoKuService;
import com.mao.his.weather.ChinaWeather;
import com.mao.service.LogService;
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
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.FaviconHandler;

/**
 * 服务开启
 * @author zongx at 2020/4/7 21:38
 */
public class MainVerticle extends AbstractVerticle {

    //服务器信息
    public static final Server server = PropertiesReader.readServer("/config/server.properties");
    //过滤路径
    public static final String[] FILTER_PATH = new String[]{"/his/*","/api/*","/file/*"};
    //图片数据储存位置前缀（至image文件夹前）
    public static final String IMAGE_FILE_LOCAL_PATH_PRE = "D:";
    //web客户端
    public static final WebClient webClient = HttpUtil.getWebClient(Vertx.vertx());

    //services
    private final MainService mainService = new MainService();
    private final LogService logService = new LogService();
    private final AuthService authService = new AuthService();
    private final BookService bookService = new BookService();
    private final BjxService bjxService = new BjxService();
    private final BuddhistService buddhistService = new BuddhistService();
    private final PicService picService = new PicService();
    private final M3U8Service m3U8Service = new M3U8Service();
    private final BaiDuMap baiDuMap = new BaiDuMap();
    private final ChinaWeather chinaWeather = new ChinaWeather();
    private final SudoKuService sudoKuService = new SudoKuService();

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route("/favicon.ico").handler(FaviconHandler.create("favicon.ico"));
        router.route().handler(CorsHandler.create("*").allowedHeader("*")).handler(mainService::filter);
        router.route("/").handler(mainService::index);
        router.route("/file/*").subRouter(file());
        if (server.isNeedAuthorize())
            router.route("/auth/*").subRouter(auth());
        router.route("/api/*").subRouter(api());
        router.route("/his/*").subRouter(his());
        router.errorHandler(404, mainService::notFound);
        router.errorHandler(401,mainService::permission);
        router.errorHandler(405,mainService::notAllowed);
        router.errorHandler(500,mainService::error);
        router.route().last().handler(logService::log);
        vertx.createHttpServer().requestHandler(router).listen(server.getPort());
    }

    /**
     * auth类请求
     */
    private Router auth(){
        Router authRouter = Router.router(vertx);
        authRouter.route("/authorize").handler(authService::authorize);
        authRouter.route("/refresh").handler(authService::refresh);
        return authRouter;
    }

    /**
     * his类请求
     */
    private Router his(){
        Router hisRouter = Router.router(vertx);
        hisRouter.get("/address/ip").handler(baiDuMap::addressIp);
        hisRouter.get("/weather/city").handler(chinaWeather::getWeather);
        hisRouter.post("/sudoKu").handler(BodyHandler.create()).handler(sudoKuService::sudoKu);
        return hisRouter;
    }

    /**
     * api类请求
     */
    private Router api(){
        Router apiRouter = Router.router(vertx);
        apiRouter.post("/data/book").handler(BodyHandler.create()).handler(bookService::getBooks);
        apiRouter.get("/data/book/:id").handler(bookService::getBook);
        apiRouter.get("/data/book/chapter/:id").handler(bookService::getChapter);
        apiRouter.get("/data/bjx").handler(BodyHandler.create()).handler(bjxService::getBjx);
        apiRouter.get("/data/bjx/:id").handler(bjxService::getBjxSrc);
        apiRouter.get("/data/buddhist").handler(BodyHandler.create()).handler(buddhistService::getBuddhists);
        apiRouter.get("/data/buddhist/:id").handler(buddhistService::getBuddhist);
        apiRouter.get("/data/buddhist/chapter/:id").handler(buddhistService::getChapter);
        apiRouter.get("/data/pic/class").handler(picService::getPicClass);
        apiRouter.get("/data/pic").handler(BodyHandler.create()).handler(picService::getPics);
        apiRouter.get("/data/pic/image/:id").handler(picService::getPic);
        apiRouter.get("/data/m3u8/live").handler(m3U8Service::live);
        apiRouter.get("/data/m3u8/movie").handler(BodyHandler.create()).handler(m3U8Service::movies);
        apiRouter.get("/data/m3u8/movie/:id").handler(m3U8Service::movie);
        return apiRouter;
    }

    /**
     * file类请求
     */
    private Router file(){
        Router fileRouter = Router.router(vertx);
        fileRouter.route("/image/*").handler(mainService::image);
        return fileRouter;
    }

}
