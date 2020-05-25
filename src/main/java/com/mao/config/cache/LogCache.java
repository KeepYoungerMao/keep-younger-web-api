package com.mao.config.cache;

import com.mao.MainVerticle;
import com.mao.config.MybatisConfig;
import com.mao.entity.log.Log;
import com.mao.mapper.sys.LoggerMapper;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 日志缓存
 * @author : create by zongx at 2020/5/25 10:59
 */
public class LogCache {

    /**
     * 线程池，此线程用于保存日志数据，应用不会太大
     */
    private static final ExecutorService service = Executors.newFixedThreadPool(4);

    /**
     * 日志数据临时缓存集合
     */
    private static final List<Log> logs = new ArrayList<>();

    /**
     * 添加此日志进日志缓存集合中
     * 判断缓存集合是否达到设定的最大值
     * 如果达到最大值，则将缓存中的日志数据进行保存。
     * 保存时使用新的集合装数据，然后使用异步执行保存。避免保存时占用logs的时间，导致锁定过长
     * 使用新的集合将logs中的数据转移过来，再将logs清空。
     * 关于logs清空：ArrayList只是将本身的size和elementData清空，并没有删除数据，他将删除数据工作交给了GC
     * 新的集合使用了这些数据，因此这些数据会等到新集合使用完后再后收，logs的clear()不会影响新的集合的数据。
     * @param log 日志数据
     */
    public static void saveLog(Log log){
        synchronized (logs){
            logs.add(log);
            if (logs.size() >= MainVerticle.application.getLog_size()){
                List<Log> saved = new ArrayList<>(logs);
                //saveLog(saved);
                logs.clear();
            }
        }
    }

    /**
     * 日志数据的保存
     * 写入数据库
     * @param logs 日志列表
     */
    private static void saveLog(List<Log> logs){
        //使用异步方式保存日志数据
        CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
            //获取可自动提交的session
            SqlSession session = MybatisConfig.getSession(true);
            LoggerMapper mapper = session.getMapper(LoggerMapper.class);
            mapper.saveLog(logs);
            session.close();
            return true;
        },service);
        future.thenAccept(saved -> System.out.println("saved logs..."));
    }

}
