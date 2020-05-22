package com.mao;

import com.mao.entity.Application;
import com.mao.entity.Server;
import com.mao.service.MainService;
import com.mao.service.auth.AuthService;
import com.mao.service.data.DataService;
import com.mao.service.his.HisService;
import com.mao.service.log.LogService;
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
    private final LogService logService = LogService.create();
    private final AuthService authService = AuthService.created();
    private final DataService dataService = DataService.created();
    private final HisService hisService = HisService.created();

    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route("/favicon.ico").handler(FaviconHandler.create("favicon.ico"));
        router.route().handler(CorsHandler.create("*").allowedHeader("*")).handler(authService::filter);
        router.route("/").handler(mainService::index);
        if (application.isNeed_authorize())
            router.route("/auth/*").subRouter(auth());
        router.route("/file/*").subRouter(file());
        router.route("/api/*").subRouter(api());

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
     * api类请求
     */
    private Router api(){
        Router apiRouter = Router.router(vertx);
        apiRouter.post("/search/data/:item").handler(BodyHandler.create()).handler(dataService::searchList);
        apiRouter.get("/search/data/:item/:id").handler(dataService::searchItem);
        apiRouter.get("/his/address/ip").handler(hisService::addressIp);
        apiRouter.get("/his/weather/city").handler(hisService::weather);
        apiRouter.post("/his/sudoKu").handler(BodyHandler.create()).handler(hisService::sudoKu);
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
