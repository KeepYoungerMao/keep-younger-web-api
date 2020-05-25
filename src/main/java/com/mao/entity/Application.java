package com.mao.entity;

import com.mao.util.SU;
import lombok.Getter;

import java.util.Properties;

/**
 * 全局配置信息
 * @author : create by zongx at 2020/5/19 17:59
 */
@Getter
public class Application {

    private final boolean need_authorize;       //是否需要授权
    private final String[] authorize_path;      //需要授权的url列表
    private final String root_path;             //本地硬盘上的文件位置
    private final int log_size;                 //日志达到该值时进行保存

    public Application(Properties properties){
        this.need_authorize = Boolean.parseBoolean(properties.getProperty("need_authorize"));
        String authorizePath = properties.getProperty("authorize_path");
        if (SU.isEmpty(authorizePath))
            this.authorize_path = new String[0];
        else {
            if (authorizePath.contains(",")){
                this.authorize_path = authorizePath.split(",");
            } else {
                this.authorize_path = new String[]{authorizePath};
            }
        }
        this.root_path = properties.getProperty("root_image_path_pre");
        this.log_size = Integer.parseInt(properties.getProperty("log_size"));
    }

}
