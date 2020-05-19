package com.mao.util;

import com.mao.entity.Application;
import com.mao.entity.Server;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置文件读取
 * @author : create by zongx at 2020/4/8 17:27
 */
public class PropertiesReader {


    public static Server readServer(String path) {
        return new Server(read(path));
    }

    public static Application readApplication(String path){
        return new Application(read(path));
    }

    /**
     * 读取Properties文件
     * 读取文件采用Class.getResourceAsStream()方法获取InputStream，可读取相对路径文件信息
     * 使用此方法需要以【/】开头，要不然查不到
     *
     * 先前尝试使用File()方式获取InputStream，但文件找不到路径。
     * @param path 地址
     * @return 服务信息
     */
    public static Properties read(String path){
        if (!path.startsWith("/"))
            path = "/" + path;
        Properties properties = new Properties();
        InputStream inputStream = PropertiesReader.class.getResourceAsStream(path);
        if (null != inputStream){
            try {
                properties.load(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return properties;
    }

}
