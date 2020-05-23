package com.mao.service.log;

import com.mao.entity.log.Log;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 日志操作
 * @author : create by zongx at 2020/4/29 14:15
 */
public class LogServiceImpl implements LogService {

    private final ExecutorService service = Executors.newFixedThreadPool(4);

    /**
     * 保存数据
     * @param log 日志数据
     */
    public void saveLog(Log log) {
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("保存日志中...");
            return true;
        },service);
        future.thenAccept(saved -> System.out.println("save log status: " + saved));
    }

}
