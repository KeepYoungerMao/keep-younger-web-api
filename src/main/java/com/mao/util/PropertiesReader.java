package com.mao.util;

import com.mao.entity.Server;
import org.apache.ibatis.io.Resources;

import java.io.*;
import java.util.*;

/**
 * 配置文件读取
 * @author : create by zongx at 2020/4/8 17:27
 */
public class PropertiesReader {

    public static Server readServer(String path) {
        Map<String, String> properties;
        try {
            properties = getProperties(path);
        } catch (IOException e) {
            e.printStackTrace();
            properties = new HashMap<>();
        }
        return new Server(properties);
    }

    /**
     * 获取properties类型配置文件信息，返回map
     * 文件读取使用的是 Mybatis 的 Resources 进行文件读取
     *
     * ========================== 改进摘要 ============================
     * 先前使用java.util.Properties.load()读取不成功：文件路劲不对
     * 又找了些获取全路径的方法，但是测试通过，jar文件启动却还是不行
     * 留给后人吧。。
     * ===============================================================
     * @param path properties文件相对路径
     * @return map数据
     * @throws IOException 读取错误抛出异常
     */
    public static Map<String, String> getProperties(String path) throws IOException {
        if (path.endsWith(".properties"))
            throw new IllegalArgumentException("file not supported");
        Reader reader = Resources.getResourceAsReader(path);
        String line;
        Map<String, String> map = new HashMap<>();
        BufferedReader bufferedReader = new BufferedReader(reader);
        while ((line = bufferedReader.readLine()) != null) {
            if (line.startsWith("#"))
                continue;
            if (line.contains("=")){
                String[] split = line.split("=",2);
                map.put(split[0],split[1]);
            } else
                throw new IllegalArgumentException("invalid properties[" + line + "]");
        }
        return map;
    }

}
