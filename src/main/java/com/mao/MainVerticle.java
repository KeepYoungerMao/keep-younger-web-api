package com.mao;

import com.mao.entity.Application;
import com.mao.entity.Server;
import com.mao.his.bd.BaiDuMap;
import com.mao.his.sudo.SudoKuService;
import com.mao.his.weather.ChinaWeather;
import com.mao.service.LogService;
import com.mao.service.MainService;
import com.mao.service.SearchDataService;
import com.mao.service.auth.AuthService;
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
    //全局配置信息
    public static final Application application = PropertiesReader.readApplication("/config/application.properties");
    //web客户端
    public static final WebClient webClient = HttpUtil.getWebClient(Vertx.vertx());

    //services
    private final MainService mainService = new MainService();
    private final LogService logService = new LogService();
    private final AuthService authService = new AuthService();
    private final BaiDuMap baiDuMap = new BaiDuMap();
    private final ChinaWeather chinaWeather = new ChinaWeather();
    private final SudoKuService sudoKuService = new SudoKuService();
    private final SearchDataService searchDataService = new SearchDataService();

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route("/favicon.ico").handler(FaviconHandler.create("favicon.ico"));
        router.route().handler(CorsHandler.create("*").allowedHeader("*")).handler(mainService::filter);
        router.route("/").handler(mainService::index);
        router.route("/file/*").subRouter(file());
        if (application.isNeed_authorize())
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
        apiRouter.post("/search/data/:item").handler(BodyHandler.create()).handler(searchDataService::searchList);
        apiRouter.get("/search/data/:item/:id").handler(searchDataService::searchItem);
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
