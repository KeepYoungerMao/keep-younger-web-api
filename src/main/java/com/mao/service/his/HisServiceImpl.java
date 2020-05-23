package com.mao.service.his;

import com.mao.his.bd.BaiDuMap;
import com.mao.his.sudo.SudoKuService;
import com.mao.his.weather.ChinaWeather;
import com.mao.service.BaseService;
import io.vertx.ext.web.RoutingContext;

/**
 * 接口调用处理类
 * 具体处理请查看具体处理类
 * @author : create by zongx at 2020/5/19 18:21
 */
public class HisServiceImpl extends BaseService implements HisService {

    private final BaiDuMap baiDuMap = new BaiDuMap();
    private final SudoKuService sudoKuService = new SudoKuService();
    private final ChinaWeather chinaWeather = new ChinaWeather();

    @Override
    public void addressIp(RoutingContext ctx) {
        baiDuMap.addressIp(ctx);
    }

    @Override
    public void weather(RoutingContext ctx) {
        chinaWeather.getWeather(ctx);
    }

    @Override
    public void sudoKu(RoutingContext ctx) {
        sudoKuService.sudoKu(ctx);
    }

}
