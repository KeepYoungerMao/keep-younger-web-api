package com.mao.config.cache;

import com.mao.entity.response.Token;
import com.mao.entity.sys.Client;
import com.mao.service.auth.AuthService;
import com.mao.util.SU;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 用户客户端认证授权的缓存
 * 使用Executor实现过期数据定时检查
 * 客户端量不能太大，否则需要考虑使用redis
 * create by mao at 2020-04-12 10:35:23
 */
public class LoginClientCache {

    private static final Map<String, Client> CLIENTS = new HashMap<>();
    private static final Map<String, Token> TOKENS = new HashMap<>();

    private LoginClientCache(){}

    //定义定时工作：每隔60秒检查过期数据
    static {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(LoginClientCache::removeTokenIfExpire,0,60, TimeUnit.SECONDS);
    }

    /**
     * 根据access_token删除Client数据
     */
    private static void removeClient(String token){
        synchronized (CLIENTS){
            CLIENTS.remove(token);
        }
    }

    /**
     * 删除过期的token
     * 如果token过期，同时删除Client数据
     */
    private static void removeTokenIfExpire(){
        synchronized (TOKENS){
            TOKENS.entrySet().removeIf(entry -> {
                Token value = entry.getValue();
                long expire = value.getTimestamp() + value.getExpire() * 1000;
                boolean b = System.currentTimeMillis() >= expire;
                if (b)
                    removeClient(value.getAccess_token());
                return b;
            });
        }
    }

    /**
     * token和client数据的保存
     * @param token token
     * @param client client
     */
    public static void save(Token token, Client client){
        client.setExpire_time(System.currentTimeMillis() + token.getExpire() * 1000);
        List<String> list = saveClient(token.getAccess_token(), client);
        saveToken(token,list);
    }

    /**
     * 保存客户端数据
     * 保存前先清除之前是否有该客户端的数据
     * 清除的时候记录清除的key：access_token
     * 并将其返回
     * @param token access_token
     * @param client 客户端数据
     */
    private static List<String> saveClient(String token, Client client){
        final List<String> list = new ArrayList<>();
        synchronized (CLIENTS){
            String client_id = client.getClient_id();
            CLIENTS.entrySet().removeIf(entry -> {
                boolean equals = entry.getValue().getClient_id().equals(client_id);
                if (equals)
                    list.add(entry.getKey());
                return equals;
            });
            CLIENTS.put(token,client);
        }
        return list;
    }

    /**
     * 保存token数据
     * @param token token数据
     * @param list 需要移除的token数据
     */
    private static void saveToken(Token token, List<String> list){
        synchronized (TOKENS){
            TOKENS.put(token.getRefresh_token(),token);
            if (list.size() > 0)
                TOKENS.entrySet().removeIf(entry -> list.contains(entry.getValue().getAccess_token()));
        }
    }

    /**
     * 获取缓存中客户端数据
     * @param token access_token
     * @return client
     */
    public static Client getClient(String token){
        synchronized (CLIENTS){
            return CLIENTS.get(token);
        }
    }

    /**
     * 获取缓存中token数据
     * @param token refresh_token
     * @return token
     */
    public static Token getToken(String token){
        synchronized (TOKENS){
            return TOKENS.get(token);
        }
    }

    /**
     * token的刷新
     * @param token refresh_token
     * @return 新token
     */
    public static Token refresh(String token){
        Token oldToken = getToken(token);
        if (null == oldToken)
            return null;
        Client client = getClient(oldToken.getAccess_token());
        if (null == client)
            return null;
        String s = AuthService.checkClient(client, false, false);
        if (null != s)
            return null;
        Token newToken = new Token(SU.uuid(),SU.uuid());
        save(newToken,client);
        return newToken;
    }

}
