package com.mao.his.bd;

import com.mao.MainVerticle;
import com.mao.service.BaseService;
import com.mao.util.SU;
import io.vertx.ext.web.RoutingContext;

/**
 * 百度地图数据请求
 * @author : create by zongx at 2020/4/9 14:15
 */
public class BaiDuMap extends BaseService {

    private static final String AK = "Po86Y8fZwYv5fpcQIX7MVk1DaMOl3VwB";

    /**
     * 根据ip地址获取地理位置api
     */
    private static final String GET_LOCATION_URL = "http://api.map.baidu.com/location/ip?ip=%s&ak=%s";

    private static final String GET_IP_ADDRESS_ERROR = "cannot request any message form SDK, please check whether ip is correct";

    /**
     * 根据ip查询该ip所在的粗略地址
     * 由百度API获取
     */
    public void addressIp(RoutingContext ctx){
        String ip = paramString(ctx,"ip");
        if (SU.isEmpty(ip))
            sendError(ctx,"param [ip] is needed");
        else if (SU.isIP(ip)){
            String url = String.format(GET_LOCATION_URL,ip,AK);
            MainVerticle.webClient.getAbs(url).send(handler -> {
                if (handler.succeeded()){
                    sendData(ctx,handler.result().bodyAsString());
                } else {
                    sendError(ctx,GET_IP_ADDRESS_ERROR);
                }
            });
        } else
            sendError(ctx,"invalid ip [" + ip + "]");
    }

}
