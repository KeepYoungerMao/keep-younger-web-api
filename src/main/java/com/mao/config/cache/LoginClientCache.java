package com.mao.config.cache;

import com.mao.entity.response.Token;
import com.mao.entity.sys.Client;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
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

    public static void save(Token token, Client client){
        saveToken(token);
        saveClient(token.getAccess_token(),client);
    }

    private static void saveClient(String token, Client client){
        synchronized (CLIENTS){
            CLIENTS.put(token,client);
        }
    }

    private static void saveToken(Token token){
        synchronized (TOKENS){
            TOKENS.put(token.getRefresh_token(),token);
        }
    }

    public static Client getClient(String token){
        synchronized (CLIENTS){
            return CLIENTS.get(token);
        }
    }

    public static Token getToken(String token){
        synchronized (TOKENS){
            return TOKENS.get(token);
        }
    }

    public static Token refresh(String token){
        return null;
    }

}
