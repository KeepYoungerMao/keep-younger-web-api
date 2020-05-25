package com.mao.service.log;

import com.mao.config.cache.LogCache;
import com.mao.entity.log.Log;

/**
 * 日志操作
 * @author : create by zongx at 2020/4/29 14:15
 */
public class LogServiceImpl implements LogService {

    /**
     * 保存数据
     * @param log 日志数据
     */
    public void saveLog(Log log) {
        LogCache.saveLog(log);
    }

}
